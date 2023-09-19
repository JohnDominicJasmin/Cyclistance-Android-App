package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event

sealed class RescueResultUiEvent{
    data object CloseRescueResults: RescueResultUiEvent()
    data object StepUp: RescueResultUiEvent()
    data object StepDown: RescueResultUiEvent()
    data class ChangeRating(val rating: Float): RescueResultUiEvent()
}
