package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event

sealed class RescueDetailsUiEvent {
    data object CloseRescueDetails: RescueDetailsUiEvent()
}