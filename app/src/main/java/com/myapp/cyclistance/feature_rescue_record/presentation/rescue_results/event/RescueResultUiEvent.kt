package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.event

sealed class RescueResultUiEvent{
    data object CloseRescueResults: RescueResultUiEvent()
    data object StepUp: RescueResultUiEvent()
    data object StepDown: RescueResultUiEvent()
    data class ChangeRating(val rating: Float): RescueResultUiEvent()
    data class ReportAccount(val id: String, val name: String, val photo: String): RescueResultUiEvent()
    data class ViewProfile(val id: String) : RescueResultUiEvent()
    data object RateRescuer: RescueResultUiEvent()
    data object ShowRescueDetails : RescueResultUiEvent()

}
