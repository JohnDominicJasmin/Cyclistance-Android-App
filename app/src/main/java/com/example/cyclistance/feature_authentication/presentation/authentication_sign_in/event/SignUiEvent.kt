package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignUiEvent {
    object DismissAlertDialog : SignUiEvent()
    object KeyboardActionDone : SignUiEvent()
    data class OnChangeEmail(val email: TextFieldValue) : SignUiEvent()
    data class OnChangePassword(val password: TextFieldValue) : SignUiEvent()
    object TogglePasswordVisibility : SignUiEvent()
    object SignInWithFacebook : SignUiEvent()
    object SignInWithGoogle : SignUiEvent()
    object SignInWithEmailAndPassword : SignUiEvent()
    object NavigateToSignUp : SignUiEvent()
    object DismissNoInternetDialog : SignUiEvent()
}
