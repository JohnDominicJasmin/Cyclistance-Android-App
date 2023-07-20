package com.example.cyclistance.feature_authentication.domain.model

import com.google.errorprone.annotations.Keep

@Keep
data class AuthenticationResult(
    val isSuccessful: Boolean = false,
    val authUser: AuthenticationUser = AuthenticationUser(),
)
