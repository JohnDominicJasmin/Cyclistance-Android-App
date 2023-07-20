package com.example.cyclistance.feature_authentication.domain.model

import com.example.cyclistance.core.domain.model.UserDetails
import com.google.errorprone.annotations.Keep

@Keep
data class AuthenticationResult(
    val isSuccessful: Boolean = false,
    val user: UserDetails = UserDetails(),
)
