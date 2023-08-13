package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
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
    val isPrivacyPolicyDialogVisible: Boolean = false,
    val urlToOpen: String? = null,

): Parcelable
