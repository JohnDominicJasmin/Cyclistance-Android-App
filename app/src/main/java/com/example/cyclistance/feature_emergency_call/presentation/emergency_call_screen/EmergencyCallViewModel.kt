package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EMERGENCY_CALL_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
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
            if (model.contacts.isEmpty()) {
                addDefaultContact()
                return@onEach
            }
            _state.update { it.copy(emergencyCallModel = model) }

        }.launchIn(viewModelScope)

    }

    fun onEvent(event: EmergencyCallVmEvent) {
        when (event) {
            is EmergencyCallVmEvent.DeleteContact -> {
                deleteContact(event.emergencyContactModel)
            }
        }
    }


    private fun deleteContact(contact: EmergencyContactModel) {
        viewModelScope.launch {
            runCatching {
                emergencyCallUseCase.deleteContactUseCase(contact)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

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

}
