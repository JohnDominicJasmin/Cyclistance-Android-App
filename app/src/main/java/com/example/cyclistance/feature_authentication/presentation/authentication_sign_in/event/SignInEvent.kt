package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event


sealed class SignInEvent {
    object RefreshEmail: SignInEvent()
    object SignInSuccess: SignInEvent()
    data class SignInFailed(val reason:String = "Sign in failed. Please try again."): SignInEvent()

    data class InvalidEmail(val reason: String = "Invalid email. Please try again."): SignInEvent()
    data class InvalidPassword(val reason: String = "Invalid password. Please try again."): SignInEvent()
    object NoInternetConnection : SignInEvent()
    object AccountBlockedTemporarily : SignInEvent()
    object ConflictFbToken : SignInEvent()
    object FacebookSignInFailed : SignInEvent()


}