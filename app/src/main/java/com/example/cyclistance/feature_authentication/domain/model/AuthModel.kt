package com.example.cyclistance.feature_authentication.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class AuthModel(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    ) : Parcelable