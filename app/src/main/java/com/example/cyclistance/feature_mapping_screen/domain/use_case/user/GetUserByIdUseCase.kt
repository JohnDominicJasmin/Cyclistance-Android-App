package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class GetUserByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String): UserItem {
        return repository.getUserById(id)
    }
}