package com.example.cyclistance.feature_rescue_record.domain.use_case

import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class GetRideHistoryUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke() = repository.getRideHistory()
}