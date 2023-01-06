package com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class DeleteRescueTransactionUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String) {
        repository.deleteRescueTransaction(id)
    }
}