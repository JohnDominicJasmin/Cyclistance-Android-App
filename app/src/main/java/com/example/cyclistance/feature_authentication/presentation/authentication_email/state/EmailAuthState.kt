package com.example.cyclistance.feature_authentication.presentation.authentication_email.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class EmailAuthState(
    val secondsLeft: Int = 0,
    val isEmailResend: Boolean = false,
    val isLoading: Boolean = false,
    val savedAccountEmail: String = "",
    ):Parcelable
