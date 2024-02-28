package com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.event

sealed class RideHistoryUiEvent{
    data class ShowRideDetails(val rideId: String) : RideHistoryUiEvent()
}
