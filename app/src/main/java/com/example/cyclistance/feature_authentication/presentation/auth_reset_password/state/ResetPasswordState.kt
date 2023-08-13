package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ResetPasswordState(
    val isLoading: Boolean = false,
):Parcelable
