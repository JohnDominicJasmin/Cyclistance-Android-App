package com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class GetRescueTransactionUpdatesUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke(): Flow<RescueTransaction> {

        return repository.getRescueTransactionUpdates()
    }


}