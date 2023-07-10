package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.ADD_EDIT_CONTACT_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.CONTACT_ID
import com.example.cyclistance.feature_emergency_call.domain.exceptions.EmergencyCallExceptions
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditVmEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactState
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
    private val savedStateHandle: SavedStateHandle,
    private val emergencyCallUseCase: EmergencyContactUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[ADD_EDIT_CONTACT_VM_STATE_KEY] ?: AddEditContactState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<AddEditEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    private val contactId: String? = savedStateHandle[CONTACT_ID]


    init {
        getContact(contactId)
        getContacts()
    }

    fun onEvent(event: AddEditVmEvent) {
        when (event) {
            is AddEditVmEvent.SaveContact -> {
                saveContact(event.emergencyContactModel)
            }
        }
        savedStateHandle[ADD_EDIT_CONTACT_VM_STATE_KEY] = state.value
    }

    private fun getContact(id: String?) {

        id ?: return
        emergencyCallUseCase.getContactUseCase(id.toInt()).catch {
            Timber.e("Error getting contact")
        }.onEach { contact ->
            _eventFlow.emit(value = AddEditEvent.GetContactSuccess(contact))
            _state.update {
                it.copy(
                    nameSnapshot = contact.name,
                    phoneNumberSnapshot = contact.phoneNumber
                )
            }
            savedStateHandle[ADD_EDIT_CONTACT_VM_STATE_KEY] = state.value
        }.launchIn(viewModelScope)

    }

    private fun getContacts() {
        emergencyCallUseCase.getContactsUseCase().catch {
            Timber.e("Error getting contacts")
        }.onEach { model ->
            _state.update { it.copy(emergencyCallModel = model) }
            savedStateHandle[ADD_EDIT_CONTACT_VM_STATE_KEY] = state.value
        }.launchIn(viewModelScope)
    }

    private fun saveContact(emergencyContactModel: EmergencyContactModel) {
        viewModelScope.launch {
            runCatching {
                emergencyCallUseCase.upsertContactUseCase(emergencyContactModel)
            }.onSuccess {
                _eventFlow.emit(value = AddEditEvent.SaveContactSuccess)
            }.onFailure {
                it.handleException()
            }
        }
    }

    private suspend fun Throwable.handleException() {
        when (this) {
            is EmergencyCallExceptions.NameException -> {
                _eventFlow.emit(value = AddEditEvent.NameFailure(this.message!!))
            }

            is EmergencyCallExceptions.PhoneNumberException -> {
                _eventFlow.emit(value = AddEditEvent.PhoneNumberFailure(this.message!!))
            }

            else -> {
                _eventFlow.emit(value = AddEditEvent.UnknownFailure(this.message!!))
            }
        }
    }

}