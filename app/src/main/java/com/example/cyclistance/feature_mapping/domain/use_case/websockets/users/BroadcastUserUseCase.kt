package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(locationModel: LiveLocationWSModel) {
        repository.broadcastUser(locationModel)
    }
}