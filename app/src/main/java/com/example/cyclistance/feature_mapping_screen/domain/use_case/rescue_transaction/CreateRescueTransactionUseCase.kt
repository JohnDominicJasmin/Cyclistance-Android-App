package com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class CreateRescueTransactionUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(rescueTransaction : RescueTransactionItem) {
        repository.createRescueTransaction(rescueTransaction)
    }
}