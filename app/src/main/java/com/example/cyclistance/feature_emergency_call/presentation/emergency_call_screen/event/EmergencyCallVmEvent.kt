package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class EmergencyCallVmEvent {

    data class SaveContact(val emergencyContactModel: EmergencyContactModel) :
        EmergencyCallVmEvent()

    data class DeleteContact(val emergencyContactModel: EmergencyContactModel) :
        EmergencyCallVmEvent()
}
