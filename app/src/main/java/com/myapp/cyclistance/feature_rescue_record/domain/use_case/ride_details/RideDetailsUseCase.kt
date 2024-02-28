package com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_details

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import kotlinx.coroutines.flow.Flow

class RideDetailsUseCase(private val repository: RescueRecordRepository) {
    operator fun invoke(): Flow<List<RideDetails>> {
        return repository.getRideDetails()
    }

    suspend operator fun invoke(rideDetails: RideDetails) {
        repository.upsertRideDetails(rideDetails = rideDetails)
    }
}