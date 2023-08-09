package com.example.cyclistance.feature_authentication.presentation.auth_email.event

sealed class EmailAuthUiEvent {
    object DismissAlertDialog : EmailAuthUiEvent()
    object VerifyEmailAuth : EmailAuthUiEvent()
    object ResendEmailAuth : EmailAuthUiEvent()
    object DismissNoInternetDialog : EmailAuthUiEvent()
}
