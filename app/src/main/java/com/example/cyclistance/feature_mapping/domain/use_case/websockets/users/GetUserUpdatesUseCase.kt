package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.api.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class GetUserUpdatesUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke(): Flow<NearbyCyclist>{
        return repository.getUserUpdates()
    }
}