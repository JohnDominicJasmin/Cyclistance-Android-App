package com.example.cyclistance.feature_rescue_record.data.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RescueRecordFlowRepositoryImpl(): RescueRecordFlowRepository {
    private val scope: CoroutineContext = Dispatchers.IO
    private var rideDetailsFlow: MutableStateFlow<RideDetails> = MutableStateFlow(RideDetails())
    private var rideMetricsFlow: MutableStateFlow<RideMetrics?> = MutableStateFlow(null)

    override suspend fun getRescueDetails(): Flow<RideDetails> {
        return withContext(scope) {
            rideDetailsFlow
        }
    }

    override suspend fun addRescueDetails(details: RideDetails) {
        withContext(scope) {
            rideDetailsFlow.emit(details)
        }
    }

    override suspend fun addRideMetrics(rideMetrics: RideMetrics) {
        withContext(scope){
            rideMetricsFlow.emit(rideMetrics)
        }
    }

    override suspend fun getRideMetrics(): Flow<RideMetrics?> {
        return withContext(scope){
            rideMetricsFlow
        }
    }
}