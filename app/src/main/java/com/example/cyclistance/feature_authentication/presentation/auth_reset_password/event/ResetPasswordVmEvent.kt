package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event

sealed class ResetPasswordVmEvent{
    data class ResetPassword(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String
    ): ResetPasswordVmEvent()
}
