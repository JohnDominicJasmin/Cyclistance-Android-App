package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignInEvent{
    object SignInFacebook: SignInEvent()
    object SignInGoogle: SignInEvent()
    object SignInDefault: SignInEvent()
    data class EnteredEmail(val email: TextFieldValue) : SignInEvent()
    data class EnteredPassword(val password: TextFieldValue) : SignInEvent()
    object ClearEmailErrorMessage: SignInEvent()
}