package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class AddRescueRespondentUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String, respondentId: String) =
        repository.addRescueRespondent(userId, respondentId)
}