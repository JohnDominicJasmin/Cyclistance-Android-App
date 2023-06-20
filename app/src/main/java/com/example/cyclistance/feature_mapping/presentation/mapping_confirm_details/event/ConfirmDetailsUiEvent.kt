package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConfirmDetailsUiEvent {
    data class ChangeAddress(val address: TextFieldValue) : ConfirmDetailsUiEvent()
    data class ChangeBikeType(val bikeType: TextFieldValue) : ConfirmDetailsUiEvent()
    data class ChangeDescription(val description: String) : ConfirmDetailsUiEvent()
    data class ChangeMessage(val message: TextFieldValue) : ConfirmDetailsUiEvent()
    object ConfirmDetails : ConfirmDetailsUiEvent()
    object CancelConfirmation : ConfirmDetailsUiEvent()
    object DismissNoInternetDialog : ConfirmDetailsUiEvent()
    object DismissBackgroundLocationDialog : ConfirmDetailsUiEvent()
    object DismissForegroundLocationDialog : ConfirmDetailsUiEvent()

}