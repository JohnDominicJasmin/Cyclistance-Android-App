package com.example.cyclistance.feature_authentication.presentation.authentication_email.event



sealed class EmailAuthEvent{
    object EmailVerificationSuccess: EmailAuthEvent()
    object EmailVerificationFailed: EmailAuthEvent()

    data class ReloadEmailFailed(val reason:String = "Failed to reload email. Please try again later."): EmailAuthEvent()
    data class EmailVerificationNotSent(val reason:String = "Email verification failed. Please try again."): EmailAuthEvent()

    object NoInternetConnection: EmailAuthEvent()
    object TimerStarted: EmailAuthEvent()
    object TimerStopped: EmailAuthEvent()
    object EmailVerificationSent: EmailAuthEvent()
    object SendEmailVerificationFailed: EmailAuthEvent()
}
