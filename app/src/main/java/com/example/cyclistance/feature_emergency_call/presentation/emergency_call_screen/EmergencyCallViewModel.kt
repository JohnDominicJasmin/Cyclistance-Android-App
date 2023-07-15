package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EMERGENCY_CALL_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.exceptions.EmergencyCallExceptions
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallVmEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmergencyCallViewModel @Inject constructor(
    private val emergencyCallUseCase: EmergencyContactUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _state =
        MutableStateFlow(savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] ?: EmergencyCallState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<EmergencyCallEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()


    init {

        emergencyCallUseCase.getContactsUseCase().catch {
            Timber.v("Error: ${it.message}")
        }.onEach { model ->

            val isPurposefullyDeleted =
                emergencyCallUseCase.areContactsPurposelyDeletedUseCase().first()
            if (model.contacts.isEmpty().and(isPurposefullyDeleted.not())) {
                addDefaultContact()
            } else {
                _state.update { it.copy(emergencyCallModel = model) }
            }
            savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] = state.value

        }.launchIn(viewModelScope)

    }

    fun onEvent(event: EmergencyCallVmEvent) {
        when (event) {
            is EmergencyCallVmEvent.DeleteContact -> deleteContact(event.emergencyContactModel)
            is EmergencyCallVmEvent.SaveContact -> saveContact(event.emergencyContactModel)
            is EmergencyCallVmEvent.GetContact -> getContact(event.id)
            is EmergencyCallVmEvent.ResetSnapshot -> resetSnapshot()

        }
        savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] = state.value
    }

    private fun resetSnapshot() {
        _state.update {
            it.copy(
                nameSnapshot = "",
                phoneNumberSnapshot = ""
            )
        }
    }

        id ?: return
        emergencyCallUseCase.getContactUseCase(id.toInt()).catch {
            Timber.e("Error getting contact")
        }.onEach { contact ->
            _eventFlow.emit(value = EmergencyCallEvent.GetContactSuccess(contact))
            _state.update {
                it.copy(
                    nameSnapshot = contact.name,
                    phoneNumberSnapshot = contact.phoneNumber
                )
            }
            savedStateHandle[EmergencyCallConstants.ADD_EDIT_CONTACT_VM_STATE_KEY] = state.value
        }.launchIn(viewModelScope)

    }


    private fun deleteContact(contact: EmergencyContactModel) {
        viewModelScope.launch {
            runCatching {
                emergencyCallUseCase.deleteContactUseCase(contact)
            }.onSuccess {
                _eventFlow.emit(value = EmergencyCallEvent.ContactDeleteSuccess)
                if (isLastContact) {
                    emergencyCallUseCase.setContactsPurposelyDeletedUseCase()
                }
            }.onFailure {
                _eventFlow.emit(value = EmergencyCallEvent.ContactDeleteFailed)
            }
        }
    }

    private val isLastContact = state.value.emergencyCallModel.contacts.size == 1

    private suspend fun addDefaultContact() {
        emergencyCallUseCase.upsertContactUseCase(
            emergencyContact = EmergencyContactModel(
                name = PHILIPPINE_RED_CROSS,
                photo = PHILIPPINE_RED_CROSS_PHOTO,
                phoneNumber = PHILIPPINE_RED_CROSS_NUMBER
            )
        )
        emergencyCallUseCase.upsertContactUseCase(
            emergencyContact = EmergencyContactModel(
                name = NATIONAL_EMERGENCY,
                photo = NATIONAL_EMERGENCY_PHOTO,
                phoneNumber = NATIONAL_EMERGENCY_NUMBER
            )
        )
    }

    private fun saveContact(emergencyContactModel: EmergencyContactModel) {
        viewModelScope.launch {
            runCatching {
                emergencyCallUseCase.upsertContactUseCase(emergencyContactModel)
            }.onSuccess {
                _eventFlow.emit(value = EmergencyCallEvent.SaveContactSuccess)
            }.onFailure {
                it.handleException()
            }
        }
    }

    private suspend fun Throwable.handleException() {
        when (this) {
            is EmergencyCallExceptions.NameException -> {
                _eventFlow.emit(value = EmergencyCallEvent.NameFailure(this.message!!))
            }

            is EmergencyCallExceptions.PhoneNumberException -> {
                _eventFlow.emit(value = EmergencyCallEvent.PhoneNumberFailure(this.message!!))
            }

            else -> {
                _eventFlow.emit(value = EmergencyCallEvent.UnknownFailure(this.message!!))
            }
        }
    }

}
