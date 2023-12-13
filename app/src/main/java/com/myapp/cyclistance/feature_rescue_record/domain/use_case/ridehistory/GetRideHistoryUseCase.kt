package com.myapp.cyclistance.feature_rescue_record.domain.use_case.ridehistory

import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository

class GetRideHistoryUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(uid: String) = repository.getRideHistory(uid = uid)
}