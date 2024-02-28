package com.myapp.cyclistance.feature_mapping.domain.use_case.user

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class DeleteRescueRespondentUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String, respondentId: String) =
        repository.deleteRescueRespondent(userId, respondentId)
}