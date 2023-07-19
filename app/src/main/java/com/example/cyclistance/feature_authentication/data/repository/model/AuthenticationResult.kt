package com.example.cyclistance.feature_authentication.data.repository.model

import com.google.errorprone.annotations.Keep

@Keep
data class AuthenticationResult(
    val isSuccessful: Boolean = false,
    val uid: String? = null
)
