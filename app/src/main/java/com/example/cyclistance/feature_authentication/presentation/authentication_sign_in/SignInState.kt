package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class SignInState(

    val isLoading: Boolean = false,

) : Parcelable
