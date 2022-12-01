package com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation_reason

data class CancellationReasonState(
    val selectedReason: String = "",
    val hasInternet: Boolean = true,
    val isLoading: Boolean = false,
    val reasonErrorMessage: String = "",
    val message: String = "",

    )