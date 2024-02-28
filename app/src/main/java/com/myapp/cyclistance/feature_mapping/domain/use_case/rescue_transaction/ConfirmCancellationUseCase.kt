package com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class ConfirmCancellationUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(rescueTransaction : RescueTransactionItem) {
        val cancellationReason = rescueTransaction.getCancellationReason()

        if(rescueTransaction.id.isNullOrEmpty()){
            throw MappingExceptions.RescueTransactionNotFoundException()
        }
        if (cancellationReason.isNullOrEmpty()){
            throw MappingExceptions.RescueTransactionReasonException()
        }
        repository.createRescueTransaction(rescueTransaction)
    }
}