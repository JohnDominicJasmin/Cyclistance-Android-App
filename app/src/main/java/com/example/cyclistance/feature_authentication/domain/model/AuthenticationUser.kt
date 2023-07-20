package com.example.cyclistance.feature_authentication.domain.model

import com.google.errorprone.annotations.Keep

@Keep
data class AuthenticationUser(
    val uid: String = "",
    val name: String = "",
    val photo: String = "",
    val email: String = "",

    )
