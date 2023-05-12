package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state

data class CancellationReasonUiState(
    val isNoInternetVisible: Boolean = false,
    val message: String = "",
    val selectedReason: String = "",
    val selectedReasonErrorMessage: String = "",
)
