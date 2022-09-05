package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import com.google.firebase.auth.AuthCredential

sealed class SignInEvent{
    data class SignInFacebook(val context: Context): SignInEvent()
    data class SignInGoogle(var authCredential: AuthCredential): SignInEvent()
    data class SignInDefault(val email: String, val password: String): SignInEvent()
    object TogglePasswordVisibility: SignInEvent()
    object DismissAlertDialog: SignInEvent()
}