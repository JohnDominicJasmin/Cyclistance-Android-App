package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

sealed class ConfirmDetailsUiEvent {
    data class ChangeAddress(val address: String) : ConfirmDetailsUiEvent()
    data class ChangeBikeType(val bikeType: String) : ConfirmDetailsUiEvent()
    data class ChangeDescription(val description: String) : ConfirmDetailsUiEvent()
    data class ChangeMessage(val message: String) : ConfirmDetailsUiEvent()
    object ConfirmDetails : ConfirmDetailsUiEvent()
    object CancelConfirmation : ConfirmDetailsUiEvent()
    object DismissNoInternetDialog : ConfirmDetailsUiEvent()
    object DismissBackgroundLocationDialog : ConfirmDetailsUiEvent()
    object DismissForegroundLocationDialog : ConfirmDetailsUiEvent()

}