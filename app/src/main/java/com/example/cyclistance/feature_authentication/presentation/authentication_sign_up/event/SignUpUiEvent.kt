package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event

sealed class SignUpUiEvent{
    object DismissAlertDialog: SignUpUiEvent()
    object KeyboardActionDone: SignUpUiEvent()
    data class ChangeEmail(val email: String): SignUpUiEvent()
    data class ChangePassword(val password: String): SignUpUiEvent()
    data class ChangeConfirmPassword(val confirmPassword: String): SignUpUiEvent()
    object TogglePasswordVisibility: SignUpUiEvent()
    object SignUpWithEmailAndPassword: SignUpUiEvent()
    object NavigateToSignIn: SignUpUiEvent()
    object DismissNoInternetDialog: SignUpUiEvent()
}
