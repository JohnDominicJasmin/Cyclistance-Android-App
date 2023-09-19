package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import kotlinx.coroutines.flow.Flow

interface RescueRecordRepository {
    suspend fun addRescueRecord(rideDetails: RideDetails)
    suspend fun getRescueDetails(): Flow<String>
    suspend fun addRescueDetails(details: String)

}