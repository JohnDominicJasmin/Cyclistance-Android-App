package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event

sealed class ResetPasswordEvent{
    object NoInternetConnection: ResetPasswordEvent()
    object ResetPasswordSuccess: ResetPasswordEvent()
    data class ResetPasswordFailed(val reason: String = "Forgot password failed. Please try again."): ResetPasswordEvent()
    data class CurrentPasswordFailed(val reason: String = "Current password is incorrect. Please try again."): ResetPasswordEvent()
    data class NewPasswordFailed(val reason: String = "New password is invalid. Please try again."): ResetPasswordEvent()
    data class ConfirmPasswordFailed(val reason: String = "Passwords do not match. Please try again."): ResetPasswordEvent()
}
