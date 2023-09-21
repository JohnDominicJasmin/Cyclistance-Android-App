package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.event

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory

sealed class RideHistoryEvent{
    data class GetRideHistorySuccess(val rideHistory: RideHistory): RideHistoryEvent()
    data class GetRideHistoryFailed(val message: String): RideHistoryEvent()
}
