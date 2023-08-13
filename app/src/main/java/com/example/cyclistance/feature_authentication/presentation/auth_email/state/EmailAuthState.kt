package com.example.cyclistance.feature_authentication.presentation.auth_email.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class EmailAuthState(
    val secondsLeft: Int = 0,
    val isEmailResend: Boolean = false,
    val isLoading: Boolean = false,
    val savedAccountEmail: String = "",
    ):Parcelable
