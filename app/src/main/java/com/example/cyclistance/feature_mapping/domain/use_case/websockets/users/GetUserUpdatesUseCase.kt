package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserUpdatesUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<NearbyCyclist>{
        return repository.getUserUpdates()
    }
}