package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.ui.focus.FocusRequester
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel

data class SignUpState(

    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var confirmPasswordErrorMessage: String = "",
    var passwordVisibility: Boolean = false,
    var focusRequester: FocusRequester = FocusRequester(),
    var isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
)
