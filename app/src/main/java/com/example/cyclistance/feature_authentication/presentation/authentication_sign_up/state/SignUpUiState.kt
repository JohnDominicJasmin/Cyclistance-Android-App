package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class SignUpUiState(
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "",
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val passwordVisible: Boolean = false,
    val isNoInternetVisible: Boolean = false,
): Parcelable
