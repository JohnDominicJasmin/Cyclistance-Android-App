package com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class CreateRescueTransactionUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(rescueTransaction : RescueTransactionItem) {
        repository.createRescueTransaction(rescueTransaction)
    }
}