package com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact

import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.isDigit
import com.example.cyclistance.feature_emergency_call.domain.exceptions.EmergencyCallExceptions
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions

class UpsertContactUseCase(
    private val repository: EmergencyContactRepository
) {

    suspend operator fun invoke(emergencyContact: EmergencyContactModel) {

        if (emergencyContact.name.isEmpty()) {
            throw EmergencyCallExceptions.NameException()
        }

        if (emergencyContact.phoneNumber.isEmpty()) {
            throw EmergencyCallExceptions.PhoneNumberException()
        }

        if (!emergencyContact.phoneNumber.isDigit()) {
            throw EmergencyCallExceptions.PhoneNumberException("Phone number must contain only numbers.")
        }


        if (emergencyContact.phoneNumber.containsSpecialCharacters()) {
            throw SettingExceptions.PhoneNumberException("Phone number must not contain special characters.")
        }



        repository.upsertContact(emergencyContact = emergencyContact)
    }
}