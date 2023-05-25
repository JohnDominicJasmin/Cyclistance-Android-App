package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastToNearbyCyclists(private val repository: MappingRepository) {
    suspend operator fun invoke(locationModel: LiveLocationWSModel) {
        repository.broadcastToNearbyCyclists(locationModel)
    }
}