package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ForgotPasswordUiState(
    val emailErrorMessage: String = "",
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetVisible: Boolean = false,
): Parcelable
