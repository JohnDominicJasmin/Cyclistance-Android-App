package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.model.api.user.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetUserByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String): UserItem {
        return repository.getUserById(id)
    }
}