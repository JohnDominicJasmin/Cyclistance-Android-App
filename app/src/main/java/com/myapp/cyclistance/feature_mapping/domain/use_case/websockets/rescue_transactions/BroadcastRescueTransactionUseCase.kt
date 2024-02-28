package com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingSocketRepository

class BroadcastRescueTransactionUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke() {
        repository.broadcastRescueTransactionToRespondent()
    }
}