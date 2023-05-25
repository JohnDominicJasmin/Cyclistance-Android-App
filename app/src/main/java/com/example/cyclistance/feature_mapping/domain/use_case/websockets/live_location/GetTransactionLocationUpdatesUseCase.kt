package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionLocationUpdatesUseCase(val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<LiveLocationWSModel>{
        return repository.getTransactionLocationUpdates()
    }
}