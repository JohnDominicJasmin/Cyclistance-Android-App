package com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class ConfirmCancellationUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(rescueTransaction : RescueTransactionItem) {
        val cancellationReason = rescueTransaction.cancellation?.cancellationReason?.reason

        if(rescueTransaction.id.isNullOrEmpty()){
            throw MappingExceptions.RescueTransactionIdException()
        }
        if (cancellationReason.isNullOrEmpty()){
            throw MappingExceptions.RescueTransactionReasonException()
        }
        repository.createRescueTransaction(rescueTransaction)
    }
}