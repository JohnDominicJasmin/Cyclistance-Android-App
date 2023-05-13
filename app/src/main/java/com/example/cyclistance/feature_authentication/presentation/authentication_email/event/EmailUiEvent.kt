package com.example.cyclistance.feature_authentication.presentation.authentication_email.event

sealed class EmailUiEvent{
    object DismissAlertDialog: EmailUiEvent()
    object VerifyEmail: EmailUiEvent()
    object ResendEmail: EmailUiEvent()
    object DismissNoInternetDialog: EmailUiEvent()
}
