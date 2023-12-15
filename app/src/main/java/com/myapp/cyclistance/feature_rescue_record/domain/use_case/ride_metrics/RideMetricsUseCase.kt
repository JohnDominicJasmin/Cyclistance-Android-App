package com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_metrics

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import kotlinx.coroutines.flow.Flow

class RideMetricsUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(): Flow<List<RideMetrics>> {
        return repository.getRideMetrics()
    }
    suspend operator fun invoke(rideMetrics: RideMetrics){
        repository.upsertRideMetrics(rideMetrics = rideMetrics)
    }
}