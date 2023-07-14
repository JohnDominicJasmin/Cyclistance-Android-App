package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class EmergencyCallEvent {
    object ContactDeleteSuccess : EmergencyCallEvent()
    object ContactDeleteFailed : EmergencyCallEvent()
    data class GetContactSuccess(val emergencyContactModel: EmergencyContactModel) :
        EmergencyCallEvent()

    object SaveContactSuccess : EmergencyCallEvent()
    data class NameFailure(val message: String) : EmergencyCallEvent()
    data class PhoneNumberFailure(val message: String) : EmergencyCallEvent()
    data class UnknownFailure(val message: String) : EmergencyCallEvent()
}