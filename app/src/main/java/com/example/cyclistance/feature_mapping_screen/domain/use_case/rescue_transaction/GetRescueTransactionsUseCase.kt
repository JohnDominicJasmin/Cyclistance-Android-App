package com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.flow

class GetRescueTransactionsUseCase(private val repository: MappingRepository){
    suspend operator fun invoke() = flow{
        emit(repository.getRescueTransactions())
    }
}