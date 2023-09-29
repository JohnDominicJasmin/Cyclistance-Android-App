package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.event

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class AddEditContactEvent{
    data object SaveContactSuccess : AddEditContactEvent()
    data class NameFailure(val message: String) : AddEditContactEvent()
    data class PhoneNumberFailure(val message: String) : AddEditContactEvent()
    data class UnknownFailure(val message: String) : AddEditContactEvent()
    data class GetContactSuccess(val emergencyContact: EmergencyContactModel) : AddEditContactEvent()
}
