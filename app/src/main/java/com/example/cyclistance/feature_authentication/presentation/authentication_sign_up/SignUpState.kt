package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.ui.focus.FocusRequester
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    val passwordVisibility: Boolean = false,
    val focusRequester: FocusRequester = FocusRequester(),
    val isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val hasAccountSignedIn: Boolean = false,
    val savedAccountEmail: String = "",
)
