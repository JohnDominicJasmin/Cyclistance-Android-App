package com.myapp.cyclistance.feature_rescue_record.domain.use_case.rescue_record

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class GetRescueRecordUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(transactionId: String): RescueRide {
        return repository.getRescueRecord(transactionId)
    }
}