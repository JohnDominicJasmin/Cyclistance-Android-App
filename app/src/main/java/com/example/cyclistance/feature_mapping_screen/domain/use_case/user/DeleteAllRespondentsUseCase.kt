package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class DeleteAllRespondentsUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String) =
        repository.deleteAllRespondents(userId)

}