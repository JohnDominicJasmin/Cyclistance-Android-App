package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionLocationUpdatesUseCase(val repository: MappingSocketRepository) {
    suspend operator fun invoke(): Flow<LiveLocationWSModel>{
        return repository.getTransactionLocationUpdates()
    }
}