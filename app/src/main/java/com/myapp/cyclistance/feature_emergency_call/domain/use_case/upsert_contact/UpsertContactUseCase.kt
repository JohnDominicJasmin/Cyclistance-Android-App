package com.myapp.cyclistance.feature_emergency_call.domain.use_case.upsert_contact

import com.myapp.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.myapp.cyclistance.core.utils.validation.InputValidate.isDigit
import com.myapp.cyclistance.feature_emergency_call.domain.exceptions.EmergencyCallExceptions
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

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
            throw EmergencyCallExceptions.PhoneNumberException("Phone number must not contain special characters.")
        }


        repository.getContacts().first().contacts.apply {

            val isNameExist = any {
                it.name.trim() == emergencyContact.name.trim() && it.id != emergencyContact.id
            }

            val isPhoneNumberExist = any {
                it.phoneNumber.trim() == emergencyContact.phoneNumber.trim() && it.id != emergencyContact.id
            }

            if (isNameExist) {
                throw EmergencyCallExceptions.NameException("Name already exists.")
            }

            if (isPhoneNumberExist) {
                throw EmergencyCallExceptions.PhoneNumberException("Phone number already exists.")
            }
        }


        repository.upsertContact(emergencyContact = emergencyContact)
        Timber.v("UpsertContactUseCase: $emergencyContact")
    }
}