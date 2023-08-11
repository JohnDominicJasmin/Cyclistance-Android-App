package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ResetPasswordUiEvent{
    object OnClickCancel: ResetPasswordUiEvent()
    object OnClickUpdate: ResetPasswordUiEvent()
    data class OnChangeCurrentPassword(val currentPassword: TextFieldValue): ResetPasswordUiEvent()
    data class OnChangeNewPassword(val newPassword: TextFieldValue): ResetPasswordUiEvent()
    data class OnChangeConfirmPassword(val confirmPassword: TextFieldValue): ResetPasswordUiEvent()
    object DismissAlertDialog: ResetPasswordUiEvent()
    object DismissNoInternetDialog: ResetPasswordUiEvent()
    object KeyboardActionDone: ResetPasswordUiEvent()
}
