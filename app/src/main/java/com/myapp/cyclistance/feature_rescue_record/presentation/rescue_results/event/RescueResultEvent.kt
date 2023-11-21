package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.event

sealed class RescueResultEvent{
    data object RatingSuccess: RescueResultEvent()
    data class RatingFailed(val message: String): RescueResultEvent()
}
