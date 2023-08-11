package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class ResetPasswordUiState(
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetVisible: Boolean = false,
    val currentPasswordErrorMessage: String = "",
    val newPasswordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
): Parcelable
