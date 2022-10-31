package com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class UpdateRescueTransactionUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id:String, rescueTransaction: RescueTransaction) {
        repository.updateRescueTransaction(id, rescueTransaction)
    }
}