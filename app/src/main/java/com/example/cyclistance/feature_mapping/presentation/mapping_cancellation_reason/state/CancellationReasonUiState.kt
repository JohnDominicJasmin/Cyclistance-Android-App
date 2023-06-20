package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state

import androidx.compose.ui.text.input.TextFieldValue

data class CancellationReasonUiState(
    val isNoInternetVisible: Boolean = false,
    val message: TextFieldValue = TextFieldValue(""),
    val selectedReason: String = "",
    val selectedReasonErrorMessage: String = "",
)
