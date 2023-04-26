package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up


sealed class SignUpUiEvent{
    object SignUpSuccess: SignUpUiEvent()
    data class CreateAccountFailed(val reason:String = "Failed to create account. Please try again later."): SignUpUiEvent()
}
