package com.myapp.cyclistance.feature_authentication.presentation.auth_forgot_password.event

sealed class ForgotPasswordEvent{
    object NoInternetConnection: ForgotPasswordEvent()
    object ForgotPasswordSuccess: ForgotPasswordEvent()
    data class ForgotPasswordFailed(val reason: String = "Forgot password failed. Please try again."): ForgotPasswordEvent()
    data class InvalidEmail(val reason:String = "Invalid email. Please try again."): ForgotPasswordEvent()
}


