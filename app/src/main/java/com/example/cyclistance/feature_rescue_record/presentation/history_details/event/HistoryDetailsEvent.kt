package com.example.cyclistance.feature_rescue_record.presentation.history_details.event

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide

sealed class HistoryDetailsEvent{
    data class GetRideDetailsSuccess(val rescueRide: RescueRide): HistoryDetailsEvent()
    data class GetRideDetailsFailed(val message: String): HistoryDetailsEvent()
}
