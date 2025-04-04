package com.myapp.cyclistance.feature_emergency_call.domain.use_case.delete_contact

import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository

class DeleteContactUseCase(
    private val repository: EmergencyContactRepository
) {
    suspend operator fun invoke(emergencyContact: EmergencyContactModel) {
        repository.deleteContact(emergencyContact = emergencyContact)
    }

}