package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class SignUpState(
    val isLoading: Boolean = false,
    val hasAccountSignedIn: Boolean = false,
    val savedAccountEmail: String = "",
):Parcelable