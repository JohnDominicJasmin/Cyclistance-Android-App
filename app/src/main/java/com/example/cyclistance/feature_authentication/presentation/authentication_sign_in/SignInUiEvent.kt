package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in



sealed class SignInUiEvent {
    object RefreshEmail: SignInUiEvent()
    object ShowNoInternetScreen : SignInUiEvent()
    object ShowMappingScreen: SignInUiEvent()
    data class ShowToastMessage(val message: String) : SignInUiEvent()


}