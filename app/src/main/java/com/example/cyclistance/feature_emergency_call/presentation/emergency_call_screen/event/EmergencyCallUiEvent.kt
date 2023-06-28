package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyContactModel

sealed class EmergencyCallUiEvent {
    object OnClickCancel : EmergencyCallUiEvent()
    object OnClickAddContact : EmergencyCallUiEvent()
    object OnClickContact : EmergencyCallUiEvent()
    data class OnClickEditContact(val id: String) : EmergencyCallUiEvent()
    data class OnClickDeleteContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class DeleteContact(val id: String) : EmergencyCallUiEvent()
    object DismissDeleteContactDialog : EmergencyCallUiEvent()


}
