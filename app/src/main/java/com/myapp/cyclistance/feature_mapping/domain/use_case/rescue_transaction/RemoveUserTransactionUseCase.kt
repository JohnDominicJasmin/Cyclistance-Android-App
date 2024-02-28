package com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class RemoveUserTransactionUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String) = repository.removeUserTransaction(id)
}