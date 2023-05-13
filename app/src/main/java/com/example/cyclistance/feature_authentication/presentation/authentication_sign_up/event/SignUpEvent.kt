package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event


sealed class SignUpEvent{
    object SignUpSuccess: SignUpEvent()
    data class CreateAccountFailed(val reason:String = "Failed to create account. Please try again later."): SignUpEvent()
    object NoInternetConnection: SignUpEvent()
    data class InvalidEmail(val reason:String = "Invalid email. Please try again."): SignUpEvent()
    data class InvalidPassword(val reason:String = "Invalid password. Please try again."): SignUpEvent()
    data class InvalidConfirmPassword(val reason:String = "Passwords do not match. Please try again."): SignUpEvent()
    object AccountAlreadyTaken: SignUpEvent()
}
