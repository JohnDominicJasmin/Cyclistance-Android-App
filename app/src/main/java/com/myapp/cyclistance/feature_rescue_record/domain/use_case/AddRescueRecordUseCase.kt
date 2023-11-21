package com.myapp.cyclistance.feature_rescue_record.domain.use_case

import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class AddRescueRecordUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(rideDetails: RideDetails) {
        repository.addRescueRecord(rideDetails = rideDetails)
    }
}