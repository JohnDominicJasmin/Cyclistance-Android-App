package com.myapp.cyclistance.feature_authentication.presentation.auth_sign_up.state

import android.os.Parcelable
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.annotations.StableState
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
