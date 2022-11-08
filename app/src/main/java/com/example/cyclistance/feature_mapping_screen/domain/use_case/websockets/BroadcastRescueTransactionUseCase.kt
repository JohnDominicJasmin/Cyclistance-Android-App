package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastRescueTransactionUseCase(private val repository: MappingRepository) {
    operator fun invoke() {
        repository.broadCastRescueTransaction()
    }
}