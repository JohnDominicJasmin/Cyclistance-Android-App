package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class NearbyCyclistsUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke(locationModel: LiveLocationSocketModel) {
        repository.broadcastToNearbyCyclists(locationModel)
    }
    suspend operator fun invoke(): Flow<NearbyCyclist> {
        return repository.getUserUpdates()
    }
}