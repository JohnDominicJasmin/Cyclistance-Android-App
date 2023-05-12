package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event

sealed class CancellationReasonEvent{
    object ConfirmCancellationReasonSuccess: CancellationReasonEvent()
    data class UnexpectedError(val reason:String = "An unexpected error occurred."): CancellationReasonEvent()
    data class UserFailed(val reason:String = "User not found"): CancellationReasonEvent()
    data class RescueTransactionFailed(val reason:String = "Rescue transaction not found"): CancellationReasonEvent()
    object NoInternetConnection: CancellationReasonEvent()
    data class InvalidCancellationReason(val reason: String): CancellationReasonEvent()

}
