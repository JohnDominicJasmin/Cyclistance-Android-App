package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class AddEditVmEvent {
    data class SaveContact(val emergencyContactModel: EmergencyContactModel) : AddEditVmEvent()
}