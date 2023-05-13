package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event

sealed class CancellationReasonUiEvent{
    data class ChangeMessage(val message: String): CancellationReasonUiEvent()
    object ConfirmCancellationReason: CancellationReasonUiEvent()
    object NavigateToMapping: CancellationReasonUiEvent()
    object DismissNoInternetDialog : CancellationReasonUiEvent()
    data class SelectReason(val reason: String) : CancellationReasonUiEvent()
}
