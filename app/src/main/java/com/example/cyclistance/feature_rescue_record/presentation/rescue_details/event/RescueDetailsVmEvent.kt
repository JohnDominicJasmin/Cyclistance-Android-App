package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event

sealed class RescueDetailsVmEvent{
    data class LoadRescueDetails(val transactionId: String): RescueDetailsVmEvent()
}