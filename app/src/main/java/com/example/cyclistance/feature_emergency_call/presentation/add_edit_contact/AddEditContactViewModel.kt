package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.ADD_EDIT_CONTACT_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EDIT_CONTACT_ID
import com.example.cyclistance.feature_emergency_call.domain.exceptions.EmergencyCallExceptions
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactEvent
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactVmEvent
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val emergencyCallUseCase: EmergencyContactUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[ADD_EDIT_CONTACT_VM_STATE_KEY] ?: AddEditContactState(
            editingContactId = savedStateHandle[EDIT_CONTACT_ID] ?: 0
        ))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddEditContactEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {

        state.value.editingContactId.takeIf { it != 0 }?.let { id ->
            getContact(id = id)
        }

    }


    private fun saveContact(emergencyContactModel: EmergencyContactModel) {
        viewModelScope.launch {
            runCatching {
                emergencyCallUseCase.upsertContactUseCase(emergencyContactModel.copy(id = state.value.editingContactId ?: 0))
            }.onSuccess {
                _eventFlow.emit(value = AddEditContactEvent.SaveContactSuccess)
            }.onFailure {
                it.handleException()
            }
        }
    }


    private fun getContact(id: Int) {
        emergencyCallUseCase.getContactUseCase(id).catch {
            Timber.e("Error getting contact")
        }.onEach { contact ->

            _eventFlow.emit(value = AddEditContactEvent.GetContactSuccess(contact))
            _state.update {
                it.copy(
                    nameSnapshot = contact.name,
                    phoneNumberSnapshot = contact.phoneNumber,
                    emergencyContact = contact
                )
            }
            savedStateHandle[EmergencyCallConstants.EMERGENCY_CALL_VM_STATE_KEY] = state.value
        }.launchIn(viewModelScope)

    }

    fun onEvent(event: AddEditContactVmEvent) {
        when (event) {
            is AddEditContactVmEvent.SaveContact -> saveContact(event.emergencyContactModel)
        }
    }

    private suspend fun Throwable.handleException() {
        when (this) {
            is EmergencyCallExceptions.NameException -> {
                _eventFlow.emit(value = AddEditContactEvent.NameFailure(this.message!!))
            }

            is EmergencyCallExceptions.PhoneNumberException -> {
                _eventFlow.emit(value = AddEditContactEvent.PhoneNumberFailure(this.message!!))
            }

            else -> {
                _eventFlow.emit(value = AddEditContactEvent.UnknownFailure(this.message!!))
            }
        }
    }
}