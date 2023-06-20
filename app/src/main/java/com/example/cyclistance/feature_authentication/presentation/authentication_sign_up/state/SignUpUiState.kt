package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class SignUpUiState(
    val email: @RawValue TextFieldValue = TextFieldValue(""),
    val emailErrorMessage:String = "",
    val password: @RawValue TextFieldValue = TextFieldValue(""),
    val passwordErrorMessage:String = "",
    val confirmPassword: @RawValue TextFieldValue = TextFieldValue(""),
    val confirmPasswordErrorMessage:String = "",
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val passwordVisible: Boolean = false,
    val isNoInternetVisible: Boolean = false,
): Parcelable
