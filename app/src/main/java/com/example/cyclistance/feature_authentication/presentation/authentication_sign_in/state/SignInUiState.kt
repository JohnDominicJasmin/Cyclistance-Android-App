package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@StableState
data class SignInUiState(
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetVisible: Boolean = false,
    val email: @RawValue TextFieldValue = TextFieldValue(""),
    val emailErrorMessage: String = "",
    val password: @RawValue TextFieldValue = TextFieldValue(""),
    val passwordErrorMessage: String = "",
    val isPasswordVisible: Boolean = false
):Parcelable

