package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val passwordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val hasInternet: Boolean = true

) : Parcelable
