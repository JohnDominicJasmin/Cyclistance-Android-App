package com.example.cyclistance.feature_alert_dialog.domain.model

import androidx.compose.runtime.Stable


@Stable
data class AlertDialogModel(
    val title: String = "",
    val description: String = "",
    val icon: Int = -1
)
