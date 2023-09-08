package com.example.cyclistance.feature_authentication.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class AuthenticationResult(
    val isSuccessful: Boolean = false,
    val user: UserDetails = UserDetails(),
):Parcelable
