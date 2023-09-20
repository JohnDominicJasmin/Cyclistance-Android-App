package com.example.cyclistance.feature_rescue_record.domain.use_case

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class GetRescueRecordUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(transactionId: String): RideDetails {
        return repository.getRescueRecord(transactionId)
    }
}