package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.rescue_transactions

import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetRescueTransactionUpdatesUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<RescueTransaction> = repository.getRescueTransactionUpdates()


}