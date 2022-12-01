package com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation_reason

sealed class CancellationReasonEvent{
    data class SelectReason(val reason: String): CancellationReasonEvent()
    object ClearReasonErrorMessage: CancellationReasonEvent()
    data class EnterMessage(val message: String): CancellationReasonEvent()
    object ConfirmCancellationReason: CancellationReasonEvent()
    object DismissNoInternetDialog: CancellationReasonEvent()
}
