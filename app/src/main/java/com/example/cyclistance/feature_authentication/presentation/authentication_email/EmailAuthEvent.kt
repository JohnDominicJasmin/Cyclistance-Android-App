package com.example.cyclistance.feature_authentication.presentation.authentication_email

sealed class EmailAuthEvent{

    object ResendButtonClick: EmailAuthEvent()
    object SendEmailVerification: EmailAuthEvent()
    object RefreshEmail: EmailAuthEvent()
    object StartTimer: EmailAuthEvent()
    object DismissNoInternetScreen: EmailAuthEvent()
    object SubscribeEmailVerification: EmailAuthEvent()
    object DismissAlertDialog: EmailAuthEvent()

}
