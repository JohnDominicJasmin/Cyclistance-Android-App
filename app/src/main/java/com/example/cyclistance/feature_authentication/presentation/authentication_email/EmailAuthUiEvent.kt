package com.example.cyclistance.feature_authentication.presentation.authentication_email



sealed class EmailAuthUiEvent{
    object EmailVerificationSuccess: EmailAuthUiEvent()
    object EmailVerificationFailed: EmailAuthUiEvent()

    data class ReloadEmailFailed(val reason:String = "Failed to reload email. Please try again later."): EmailAuthUiEvent()
    data class EmailVerificationNotSent(val reason:String = "Email verification failed. Please try again."): EmailAuthUiEvent()
}
