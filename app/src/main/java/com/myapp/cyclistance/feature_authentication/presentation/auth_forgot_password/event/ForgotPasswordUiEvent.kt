package com.myapp.cyclistance.feature_authentication.presentation.auth_forgot_password.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ForgotPasswordUiEvent{
    object OnClickCancel: ForgotPasswordUiEvent()
    object OnClickSubmit: ForgotPasswordUiEvent()
    object ClearEmailInput: ForgotPasswordUiEvent()
    data class OnChangeEmail(val email: TextFieldValue): ForgotPasswordUiEvent()
    object DismissAlertDialog: ForgotPasswordUiEvent()
    object DismissNoInternetDialog: ForgotPasswordUiEvent()
    object KeyboardActionDone: ForgotPasswordUiEvent()
}
