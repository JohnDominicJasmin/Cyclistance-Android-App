package com.example.cyclistance.feature_rescue_record.domain.repository

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails

interface RescueRecordRepository {
    suspend fun addRescueRecord(rideDetails: RideDetails)
}