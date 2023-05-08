package com.example.cyclistance.feature_authentication.presentation.authentication_email

sealed class EmailAuthEvent{

    object SendEmailVerification: EmailAuthEvent()
    object ResendEmailVerification: EmailAuthEvent()
    object RefreshEmail: EmailAuthEvent()
    object StartTimer: EmailAuthEvent()
    object SubscribeEmailVerification: EmailAuthEvent()

}
