package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.content.Context
import com.google.firebase.auth.AuthCredential

sealed class SignInEvent{
    data class SignInFacebook(val context: Context): SignInEvent() // todo: remove context
    data class SignInGoogle(var authCredential: AuthCredential): SignInEvent()
    object SignInDefault: SignInEvent()
    data class EnterEmail(val email: String) : SignInEvent()
    data class EnterPassword(val password: String) : SignInEvent()
    object TogglePasswordVisibility: SignInEvent()
    object DismissAlertDialog : SignInEvent()
}