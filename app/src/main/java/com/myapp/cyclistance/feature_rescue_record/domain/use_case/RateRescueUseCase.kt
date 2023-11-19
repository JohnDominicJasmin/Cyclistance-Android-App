package com.myapp.cyclistance.feature_rescue_record.domain.use_case

import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class RateRescueUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(rescueId: String, rating: Double, ratingText: String) {
        repository.rateRescue(rescueId, rating, ratingText)
    }
}