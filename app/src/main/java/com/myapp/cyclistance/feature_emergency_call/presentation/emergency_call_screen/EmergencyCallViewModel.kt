package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.utils.constants.EmergencyCallConstants
import com.myapp.cyclistance.core.utils.constants.EmergencyCallConstants.EMERGENCY_CALL_VM_STATE_KEY
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallVmEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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




    private fun loadDefaultContacts(){

        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            emergencyCallUseCase.getContactsUseCase().collect { model ->
                val isPurposefullyDeleted = emergencyCallUseCase.areContactsPurposelyDeletedUseCase().first()
                if (model.contacts.isEmpty().and(isPurposefullyDeleted.not())) {

                    addDefaultContact()

                } else {
                    _state.update { it.copy(emergencyCallModel = model) }
                }
                savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] = state.value
            }
        }
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
                    name = EmergencyCallConstants.NATIONAL_EMERGENCY,
                    photo = EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO,
                    phoneNumber = EmergencyCallConstants.NATIONAL_EMERGENCY_NUMBER
                )
            )
            emergencyCallUseCase.upsertContactUseCase(
                emergencyContact = EmergencyContactModel(
                    name = EmergencyCallConstants.PHILIPPINE_RED_CROSS,
                    photo = EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO,
                    phoneNumber = EmergencyCallConstants.PHILIPPINE_RED_CROSS_NUMBER
                )
            )
        emergencyCallUseCase.setContactsPurposelyDeletedUseCase()

    }


    fun onEvent(event: EmergencyCallVmEvent) {
        when (event) {
            is EmergencyCallVmEvent.DeleteContact -> deleteContact(event.emergencyContactModel)
            EmergencyCallVmEvent.LoadDefaultContact -> loadDefaultContacts()
        }
        savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] = state.value
    }



}
