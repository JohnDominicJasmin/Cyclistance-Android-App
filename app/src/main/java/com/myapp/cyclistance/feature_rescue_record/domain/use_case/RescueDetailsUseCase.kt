package com.myapp.cyclistance.feature_rescue_record.domain.use_case

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordFlowRepository
import kotlinx.coroutines.flow.Flow

class RescueDetailsUseCase(private val repository: RescueRecordFlowRepository) {
    suspend operator fun invoke(): Flow<RideDetails> {
        return repository.getRescueDetails()
    }
    suspend operator fun invoke(details:RideDetails){
        repository.addRescueDetails(details)
    }
}