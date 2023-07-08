package com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact

import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import javax.inject.Inject

class UpsertContactUseCase @Inject constructor(
    private val repository: EmergencyContactRepository
) {

    suspend operator fun invoke(emergencyContact: EmergencyContactModel) {
        repository.upsertContact(emergencyContact = emergencyContact)
    }
}