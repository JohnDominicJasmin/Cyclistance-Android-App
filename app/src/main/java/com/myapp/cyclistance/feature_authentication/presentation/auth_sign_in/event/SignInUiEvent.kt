package com.myapp.cyclistance.feature_authentication.presentation.auth_sign_in.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignInUiEvent {
    object DismissAlertDialog : SignInUiEvent()
    object KeyboardActionDone : SignInUiEvent()
    data class OnChangeEmail(val email: TextFieldValue) : SignInUiEvent()
    data class OnChangePassword(val password: TextFieldValue) : SignInUiEvent()
    object TogglePasswordVisibility : SignInUiEvent()
    object SignInWithFacebook : SignInUiEvent()
    object SignInWithGoogle : SignInUiEvent()
    object SignInWithEmailAndPassword : SignInUiEvent()
    object NavigateToSignUp : SignInUiEvent()
    object NavigateToForgotPassword : SignInUiEvent()
    object DismissNoInternetDialog : SignInUiEvent()
    data class SetPrivacyPolicyVisibility(val isVisible: Boolean) : SignInUiEvent()
    object AgreedToPrivacyPolicy : SignInUiEvent()
    object DismissWebView : SignInUiEvent()
    data class OpenWebView(val url: String) : SignInUiEvent()
}
