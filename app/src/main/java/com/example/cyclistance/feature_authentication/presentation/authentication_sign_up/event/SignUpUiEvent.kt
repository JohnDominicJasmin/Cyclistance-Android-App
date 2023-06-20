package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignUpUiEvent{
    object DismissAlertDialog: SignUpUiEvent()
    object KeyboardActionDone: SignUpUiEvent()
    data class ChangeEmail(val email: TextFieldValue): SignUpUiEvent()
    data class ChangePassword(val password: TextFieldValue): SignUpUiEvent()
    data class ChangeConfirmPassword(val confirmPassword: TextFieldValue): SignUpUiEvent()
    object TogglePasswordVisibility: SignUpUiEvent()
    object SignUpWithEmailAndPassword: SignUpUiEvent()
    object NavigateToSignIn: SignUpUiEvent()
    object DismissNoInternetDialog: SignUpUiEvent()
}
