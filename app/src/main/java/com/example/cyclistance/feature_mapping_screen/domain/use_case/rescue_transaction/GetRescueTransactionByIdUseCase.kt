package com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class GetRescueTransactionByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String) = repository.getRescueTransactionById(id)

}