package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event

sealed class SignUiEvent{
    object DismissAlertDialog: SignUiEvent()
    object KeyboardActionDone: SignUiEvent()
    data class ChangeEmail(val email: String): SignUiEvent()
    data class ChangePassword(val password: String): SignUiEvent()
    object TogglePasswordVisibility: SignUiEvent()
    object SignInWithFacebook: SignUiEvent()
    object SignInWithGoogle: SignUiEvent()
    object SignInWithEmailAndPassword: SignUiEvent()
    object NavigateToSignUp: SignUiEvent()
    object DismissNoInternetDialog: SignUiEvent()
}
