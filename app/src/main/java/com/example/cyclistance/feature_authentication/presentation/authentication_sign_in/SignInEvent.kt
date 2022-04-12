package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import com.google.firebase.auth.AuthCredential

sealed class SignInEvent{
    data class SignInFacebook(val context: Context): SignInEvent()
    data class SignInGoogle(var authCredential: AuthCredential): SignInEvent()
    object SignInDefault: SignInEvent()
    data class EnteredEmail(val email: TextFieldValue) : SignInEvent()
    data class EnteredPassword(val password: TextFieldValue) : SignInEvent()
    object ClearEmail: SignInEvent()
    object TogglePasswordVisibility: SignInEvent()
}