package com.example.cyclistance.feature_authentication.presentation.authentication_email


data class EmailAuthState(
    var secondsLeft: Int = 0,
    var isTimerRunning: Boolean = false,
    var isEmailResendClicked: Boolean = false,
    var isLoading: Boolean = false
    )
