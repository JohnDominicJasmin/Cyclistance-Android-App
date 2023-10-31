package com.example.cyclistance.feature_rescue_record.domain.use_case

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class AddRideMetricsUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(rideId: String, rideMetrics: RideMetrics){
        repository.addRideMetrics(rideId = rideId, rideMetrics = rideMetrics)
    }
}