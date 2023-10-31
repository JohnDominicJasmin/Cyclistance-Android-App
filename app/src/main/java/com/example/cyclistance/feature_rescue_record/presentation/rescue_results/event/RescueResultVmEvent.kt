package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event

sealed class RescueResultVmEvent{
    data class RateRescue(val rating: Float): RescueResultVmEvent()
    data object UpdateUserStats: RescueResultVmEvent()
}
