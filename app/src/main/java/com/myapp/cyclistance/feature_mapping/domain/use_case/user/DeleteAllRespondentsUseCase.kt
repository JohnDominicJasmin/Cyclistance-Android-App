package com.myapp.cyclistance.feature_mapping.domain.use_case.user

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class DeleteAllRespondentsUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String) =
        repository.deleteAllRespondents(userId)

}