package com.example.cyclistance.feature_authentication.presentation.auth_sign_up.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignUpUiEvent {
    object DismissAlertDialog : SignUpUiEvent()
    object KeyboardActionDone : SignUpUiEvent()
    data class OnChangeEmail(val email: TextFieldValue) : SignUpUiEvent()
    data class OnChangePassword(val password: TextFieldValue) : SignUpUiEvent()
    data class OnChangeConfirmPassword(val confirmPassword: TextFieldValue) : SignUpUiEvent()
    object TogglePasswordVisibility : SignUpUiEvent()
    object SignUpWithEmailAndPassword : SignUpUiEvent()
    object NavigateToSignIn : SignUpUiEvent()
    object DismissNoInternetDialog : SignUpUiEvent()

    data class SetPrivacyPolicyVisibility(val isVisible: Boolean) : SignUpUiEvent()
    object AgreedToPrivacyPolicy : SignUpUiEvent()
    object DismissWebView : SignUpUiEvent()
    data class OpenWebView(val url: String) : SignUpUiEvent()
}
