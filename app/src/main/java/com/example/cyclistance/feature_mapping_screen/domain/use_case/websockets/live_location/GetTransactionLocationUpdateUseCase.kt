package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class GetTransactionLocationUpdateUseCase(private val repository: MappingRepository) {
    operator fun invoke() = repository.getTransactionLocationUpdates()
}