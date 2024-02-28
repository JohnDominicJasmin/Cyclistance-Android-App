package com.myapp.cyclistance.feature_authentication.presentation.auth_forgot_password.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class ForgotPasswordState(
    val isLoading: Boolean = false
):Parcelable
