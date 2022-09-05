package com.example.cyclistance.feature_authentication.presentation.authentication_email

import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel


data class EmailAuthState(
    val secondsLeft: Int = 0,
    val isTimerRunning: Boolean = false,
    val isEmailResendClicked: Boolean = false,
    val isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel()
    )
