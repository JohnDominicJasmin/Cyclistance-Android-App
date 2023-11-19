package com.myapp.cyclistance.feature_rescue_record.domain.use_case

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordFlowRepository

class RideMetricsUseCase(private val repository: RescueRecordFlowRepository) {
    suspend operator fun invoke() = repository.getRideMetrics()
    suspend operator fun invoke(rideMetrics: RideMetrics) =
        repository.addRideMetrics(rideMetrics = rideMetrics)
}