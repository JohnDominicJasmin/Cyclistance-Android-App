package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class AddEditEvent {
    data class GetContactSuccess(val emergencyContactModel: EmergencyContactModel) : AddEditEvent()
    object SaveContactSuccess : AddEditEvent()
    data class NameFailure(val message: String) : AddEditEvent()
    data class PhoneNumberFailure(val message: String) : AddEditEvent()
    data class UnknownFailure(val message: String) : AddEditEvent()
}
