package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

sealed class CancellationReasonEvent{
    data class ConfirmCancellationReason(
        val reason: String,
        val message: String,
    ): CancellationReasonEvent()

}
