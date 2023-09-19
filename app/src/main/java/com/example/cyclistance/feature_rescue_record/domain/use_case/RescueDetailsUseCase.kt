package com.example.cyclistance.feature_rescue_record.domain.use_case

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import kotlinx.coroutines.flow.Flow

class RescueDetailsUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(): Flow<RideDetails> {
        return repository.getRescueDetails()
    }
    suspend operator fun invoke(details:RideDetails){
        repository.addRescueDetails(details)
    }
}