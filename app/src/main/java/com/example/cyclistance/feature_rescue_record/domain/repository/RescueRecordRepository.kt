package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import kotlinx.coroutines.flow.Flow

interface RescueRecordRepository {
    suspend fun addRescueRecord(rideDetails: RideDetails)
    suspend fun getRescueRecord(transactionId: String): RideDetails
    suspend fun getRescueDetails(): Flow<RideDetails>
    suspend fun getRideHistory(uid: String): RideHistory
    suspend fun addRescueDetails(details: RideDetails)

}