package com.myapp.cyclistance.feature_authentication.presentation.auth_sign_in.state

import android.os.Parcelable
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class SignInUiState(
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetVisible: Boolean = false,
    val isPrivacyPolicyDialogVisible: Boolean = false,
    val urlToOpen: String? = null,
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val isPasswordVisible: Boolean = false
):Parcelable

