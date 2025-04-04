package com.myapp.cyclistance.feature_rescue_record.domain.use_case.rate

import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class RateRescuerUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(rescuerId: String, rating: Double){
        repository.rateRescuer(rescuerId = rescuerId, rating = rating)
    }
}