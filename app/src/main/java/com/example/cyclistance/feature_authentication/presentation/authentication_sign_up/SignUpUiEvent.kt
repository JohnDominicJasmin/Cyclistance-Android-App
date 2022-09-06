package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up


sealed class SignUpUiEvent{
    object ShowNoInternetScreen: SignUpUiEvent()
    object ShowEmailAuthScreen: SignUpUiEvent()
    data class ShowToastMessage(val message: String) : SignUpUiEvent()

}
