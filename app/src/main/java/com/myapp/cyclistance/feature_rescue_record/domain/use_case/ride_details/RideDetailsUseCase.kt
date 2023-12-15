package com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_details

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class RideDetailsUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke() {
        repository.getRideDetails()
    }
    suspend fun invoke(rideDetails: RideDetails) {
        repository.upsertRideDetails(rideDetails = rideDetails)
    }
}