package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping_screen.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastTransactionLocationUseCase(private val repository: MappingRepository) {
    operator fun invoke(locationModel: LiveLocationWSModel) {
        repository.broadcastLocation(locationModel)
    }
}