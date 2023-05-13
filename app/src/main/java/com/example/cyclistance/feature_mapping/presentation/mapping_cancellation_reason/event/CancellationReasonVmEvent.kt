package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event

sealed class CancellationReasonVmEvent{
    data class ConfirmCancellationReason(
        val reason: String,
        val message: String,
    ): CancellationReasonVmEvent()

}
