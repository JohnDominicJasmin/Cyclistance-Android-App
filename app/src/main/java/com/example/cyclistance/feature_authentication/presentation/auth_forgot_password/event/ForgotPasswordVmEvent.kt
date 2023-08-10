package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event

sealed class ForgotPasswordVmEvent{
    data class SendPasswordResetEmail(
        val email: String,
    ): ForgotPasswordVmEvent()
}
