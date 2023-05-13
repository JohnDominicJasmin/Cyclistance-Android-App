package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.event

sealed class RescueRequestUiEvent{
    data class CancelRequestHelp(val id: String): RescueRequestUiEvent()
    data class ConfirmRequestHelp(val id: String): RescueRequestUiEvent()
    object DismissAlertDialog: RescueRequestUiEvent()
    object DismissNoInternetDialog: RescueRequestUiEvent()
}
