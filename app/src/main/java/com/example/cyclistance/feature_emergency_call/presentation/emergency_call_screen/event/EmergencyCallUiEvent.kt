package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

sealed class EmergencyCallUiEvent {
    object OnClickCancel : EmergencyCallUiEvent()
    object OnClickAddContact : EmergencyCallUiEvent()
    object OnClickContact : EmergencyCallUiEvent()
    data class OnClickEditContact(val id: String) : EmergencyCallUiEvent()
    data class OnClickDeleteContact(val id: String) : EmergencyCallUiEvent()


}
