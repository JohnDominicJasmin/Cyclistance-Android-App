package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConfirmDetailsUiEvent {
    data class OnChangeAddress(val address: TextFieldValue) : ConfirmDetailsUiEvent()
    data class OnChangeBikeType(val bikeType: TextFieldValue) : ConfirmDetailsUiEvent()
    data class OnChangeDescription(val description: String) : ConfirmDetailsUiEvent()
    data class OnChangeMessage(val message: TextFieldValue) : ConfirmDetailsUiEvent()
    data object ConfirmDetails : ConfirmDetailsUiEvent()
    data object CancelConfirmation : ConfirmDetailsUiEvent()
    data object DismissNoInternetDialog : ConfirmDetailsUiEvent()
    data object DismissBackgroundLocationDialog : ConfirmDetailsUiEvent()
    data object DismissForegroundLocationDialog : ConfirmDetailsUiEvent()
    data object DismissProminentForegroundLocationDialog : ConfirmDetailsUiEvent()
    data object DismissProminentBackgroundLocationDialog : ConfirmDetailsUiEvent()
    data object AllowProminentForegroundLocationDialog : ConfirmDetailsUiEvent()
    data object AllowProminentBackgroundLocationDialog : ConfirmDetailsUiEvent()

}