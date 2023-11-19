package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class EmergencyCallUiEvent {
    data object OnClickAddContact : EmergencyCallUiEvent()
    data class OnClickContact(val phoneNumber: String) : EmergencyCallUiEvent()
    data class OnClickEditContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class OnClickDeleteContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class DeleteContact(val emergencyContact: EmergencyContactModel) : EmergencyCallUiEvent()
    data object DismissDeleteContactDialog : EmergencyCallUiEvent()
    data object DismissMaximumContactDialog : EmergencyCallUiEvent()








}
