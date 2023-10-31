package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import kotlinx.coroutines.flow.Flow

interface RescueRecordFlowRepository {
    suspend fun getRescueDetails(): Flow<RideDetails>
    suspend fun addRescueDetails(details: RideDetails)
    suspend fun addRideMetrics(rideMetrics: RideMetrics)
    suspend fun getRideMetrics(): Flow<RideMetrics?>
}