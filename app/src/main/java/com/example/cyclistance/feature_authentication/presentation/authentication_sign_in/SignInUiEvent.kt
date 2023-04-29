package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in


sealed class SignInUiEvent {
    object RefreshEmail: SignInUiEvent()
    object SignInSuccess: SignInUiEvent()
    data class SignInFailed(val reason:String = "Sign in failed. Please try again."): SignInUiEvent()

    data class InvalidEmail(val reason: String = "Invalid email. Please try again."): SignInUiEvent()
    data class InvalidPassword(val reason: String = "Invalid password. Please try again."): SignInUiEvent()
    object NoInternetConnection : SignInUiEvent()
    object AccountBlockedTemporarily : SignInUiEvent()
    object ConflictFbToken : SignInUiEvent()
    object FacebookSignInFailed : SignInUiEvent()


}