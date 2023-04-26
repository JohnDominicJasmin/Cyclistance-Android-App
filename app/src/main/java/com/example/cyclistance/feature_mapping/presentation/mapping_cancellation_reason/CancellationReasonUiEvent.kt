package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

sealed class CancellationReasonUiEvent{
    object ConfirmCancellationReasonSuccess: CancellationReasonUiEvent()
    data class UnexpectedError(val reason:String = "An unexpected error occurred."): CancellationReasonUiEvent()
    data class UserFailed(val reason:String = "User not found"): CancellationReasonUiEvent()
    data class RescueTransactionFailed(val reason:String = "Rescue transaction not found"): CancellationReasonUiEvent()
}
