package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConfirmDetailsUiEvent {
    data class OnChangeAddress(val address: TextFieldValue) : ConfirmDetailsUiEvent()
    data class OnChangeBikeType(val bikeType: TextFieldValue) : ConfirmDetailsUiEvent()
    data class OnChangeDescription(val description: String) : ConfirmDetailsUiEvent()
    data class OnChangeMessage(val message: TextFieldValue) : ConfirmDetailsUiEvent()
    object ConfirmDetails : ConfirmDetailsUiEvent()
    object CancelConfirmation : ConfirmDetailsUiEvent()
    object DismissNoInternetDialog : ConfirmDetailsUiEvent()
    object DismissBackgroundLocationDialog : ConfirmDetailsUiEvent()
    object DismissForegroundLocationDialog : ConfirmDetailsUiEvent()

}