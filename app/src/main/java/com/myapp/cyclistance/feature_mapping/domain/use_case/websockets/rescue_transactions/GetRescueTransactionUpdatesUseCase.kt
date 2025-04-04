package com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class GetRescueTransactionUpdatesUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke(): Flow<RescueTransaction> {

        return repository.getRescueTransactionUpdates()
    }
}