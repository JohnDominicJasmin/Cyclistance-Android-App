package com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastRescueTransactionUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke() {
        repository.broadcastRescueTransaction()
    }
}