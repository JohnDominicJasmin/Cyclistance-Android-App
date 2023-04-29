package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up


sealed class SignUpUiEvent{
    object SignUpSuccess: SignUpUiEvent()
    data class CreateAccountFailed(val reason:String = "Failed to create account. Please try again later."): SignUpUiEvent()
    object NoInternetConnection: SignUpUiEvent()
    data class InvalidEmail(val reason:String = "Invalid email. Please try again."): SignUpUiEvent()
    data class InvalidPassword(val reason:String = "Invalid password. Please try again."): SignUpUiEvent()
    data class InvalidConfirmPassword(val reason:String = "Passwords do not match. Please try again."): SignUpUiEvent()
    object AccountAlreadyTaken: SignUpUiEvent()
}
