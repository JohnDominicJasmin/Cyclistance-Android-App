package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository

class BroadcastToNearbyCyclists(private val repository: MappingSocketRepository) {
    suspend operator fun invoke(locationModel: LiveLocationWSModel) {
        repository.broadcastToNearbyCyclists(locationModel)
    }
}