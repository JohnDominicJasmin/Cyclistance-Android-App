package com.myapp.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted

import com.myapp.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository

class AreContactsPurposelyDeletedUseCase(
    private val repository: EmergencyContactRepository
) {
    suspend operator fun invoke() = repository.areContactsPurposelyDeleted()
}