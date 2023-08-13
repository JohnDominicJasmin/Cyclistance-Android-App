package com.example.cyclistance.feature_authentication.presentation.auth_email.event

sealed class EmailAuthVmEvent{

    object SendEmailVerification: EmailAuthVmEvent()
    object ResendEmailVerification: EmailAuthVmEvent()
    object RefreshEmail: EmailAuthVmEvent()
    object StartTimer: EmailAuthVmEvent()
    object SubscribeEmailVerification: EmailAuthVmEvent()

}
