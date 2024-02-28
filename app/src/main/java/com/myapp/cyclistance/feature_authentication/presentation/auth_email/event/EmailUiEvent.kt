package com.myapp.cyclistance.feature_authentication.presentation.auth_email.event

sealed class EmailUiEvent{
    object DismissAlertDialog: EmailUiEvent()
    object VerifyEmail: EmailUiEvent()
    object ResendEmail: EmailUiEvent()
    object DismissNoInternetDialog: EmailUiEvent()
}
