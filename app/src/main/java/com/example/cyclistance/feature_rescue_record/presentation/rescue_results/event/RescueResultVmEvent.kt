package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event

sealed class RescueResultVmEvent{
    data class RateRescuer(val rating: Float): RescueResultVmEvent()
}
