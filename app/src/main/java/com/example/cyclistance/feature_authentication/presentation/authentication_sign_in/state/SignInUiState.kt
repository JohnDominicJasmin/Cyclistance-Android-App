package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class SignInUiState(
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetVisible : Boolean = false,
    val email: String = "",
    val emailErrorMessage: String = "",
    val password: String = "",
    val passwordErrorMessage: String = "",
    val isPasswordVisible: Boolean = false
):Parcelable

