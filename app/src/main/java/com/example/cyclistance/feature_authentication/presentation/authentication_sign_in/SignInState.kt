package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.ui.focus.FocusRequester
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val passwordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val focusRequester: FocusRequester = FocusRequester(),
    val alertDialogModel: AlertDialogModel = AlertDialogModel()

    )
