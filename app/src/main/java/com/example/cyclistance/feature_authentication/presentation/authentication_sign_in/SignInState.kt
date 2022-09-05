package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel

data class SignInState(

    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var passwordVisibility: Boolean = false,
    var isLoading: Boolean = false,
    val focusRequester: FocusRequester = FocusRequester(),
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),

    )
