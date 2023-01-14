package com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import android.content.Context
import com.example.cyclistance.feature_mapping.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetRescueTransactionUpdatesUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<RescueTransaction> {

        return repository.getRescueTransactionUpdates()
    }


}