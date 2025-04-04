package com.myapp.cyclistance.feature_authentication.presentation.auth_sign_in.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class SignInState(
    val isLoading: Boolean = false,
    val userAgreedToPrivacyPolicy: Boolean = false,
) : Parcelable
