package com.myapp.cyclistance.feature_rescue_record.presentation.history_details.event

sealed class HistoryDetailsVmEvent{
    data class LoadRideDetails(val transactionId: String): HistoryDetailsVmEvent()
}
