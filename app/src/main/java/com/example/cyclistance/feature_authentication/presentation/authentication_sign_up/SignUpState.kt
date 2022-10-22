package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import android.os.Parcelable
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    val passwordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val hasAccountSignedIn: Boolean = false,
    val savedAccountEmail: String = "",
    val hasInternet: Boolean = true,
):Parcelable
