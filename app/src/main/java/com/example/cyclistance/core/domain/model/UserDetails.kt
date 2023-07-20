package com.example.cyclistance.core.domain.model

import com.google.errorprone.annotations.Keep

@Keep
data class UserDetails(
    val uid: String = "",
    val name: String = "",
    val photo: String = "",
    val email: String = "",
)
