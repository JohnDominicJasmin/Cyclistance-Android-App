package com.example.cyclistance.feature_authentication.domain.model

import com.google.errorprone.annotations.Keep

@Keep
data class AuthModel(
    val email:String = "",
    val password:String = "",
    val confirmPassword:String = "",

    )