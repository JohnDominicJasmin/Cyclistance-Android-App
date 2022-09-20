package com.example.cyclistance.feature_authentication.presentation.authentication_email

import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel

@Stable
data class EmailAuthState(
    val secondsLeft: Int = 0,
    val isTimerRunning: Boolean = false,
    val isEmailResendClicked: Boolean = false,
    val isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val savedAccountEmail: String = "",
    val hasInternet: Boolean = true
    )
