package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class CancellationReasonUiEvent{
    data class OnChangeMessage(val message: TextFieldValue) : CancellationReasonUiEvent()
    object ConfirmCancellationReason: CancellationReasonUiEvent()
    object NavigateToMapping: CancellationReasonUiEvent()
    object DismissNoInternetDialog : CancellationReasonUiEvent()
    data class SelectReason(val reason: String) : CancellationReasonUiEvent()
}
