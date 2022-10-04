package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class GetUserByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String): User {
        return repository.getUserById(id)
    }
}