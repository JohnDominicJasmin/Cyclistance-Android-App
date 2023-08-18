package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String): Flow<UserItem> {
        return repository.getUserById(id)
    }
}