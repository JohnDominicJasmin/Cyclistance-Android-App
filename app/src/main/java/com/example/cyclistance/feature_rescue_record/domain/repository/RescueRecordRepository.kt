package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import com.example.cyclistance.feature_user_profile.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface RescueRecordRepository {
    suspend fun addRescueRecord(rideDetails: RideDetails)
    suspend fun getRescueRecord(transactionId: String): RideDetails
    suspend fun rateRescue(rescueId: String, rating: Double, ratingText: String)
    suspend fun rateRescuer(rescuerId: String, rating: Double)
    suspend fun getRescueDetails(): Flow<RideDetails>
    suspend fun getRideHistory(uid: String): RideHistory
    suspend fun addRescueDetails(details: RideDetails)
    suspend fun updateStats(userStats: UserStats)
}