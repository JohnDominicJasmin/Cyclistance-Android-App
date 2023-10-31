package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_user_profile.domain.model.UserStats

interface RescueRecordRepository {
    suspend fun addRescueRecord(rideDetails: RideDetails)
    suspend fun getRescueRecord(transactionId: String): RescueRide

    suspend fun rateRescue(rescueId: String, rating: Double, ratingText: String)
    suspend fun rateRescuer(rescuerId: String, rating: Double)

    suspend fun getRideHistory(uid: String): RideHistory
    suspend fun updateStats(userStats: UserStats)

    suspend fun addRideMetrics(rideId: String, rideMetrics: RideMetrics)
}