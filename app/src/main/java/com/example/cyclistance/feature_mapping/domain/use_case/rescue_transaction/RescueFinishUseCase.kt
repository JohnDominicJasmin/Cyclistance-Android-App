package com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.StatusModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import java.util.Date

class RescueFinishUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(transactionId: String){

        val rescueTransaction = RescueTransactionItem(
            id = transactionId,
            status = StatusModel(finished = true, onGoing = false),
            endingMillis = Date().time
        )
        repository.createRescueTransaction(rescueTransaction = rescueTransaction)
    }
}