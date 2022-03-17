package com.example.cyclistance.feature_authentication.presentation.common

data class InputResultState<T>(

    val authState: AuthState<T>? = null,
    val emailExceptionMessage:String = "",
    val passwordExceptionMessage:String = "",
    val confirmPasswordExceptionMessage:String = "",
    val internetExceptionMessage:String = "",
    val invalidUserExceptionMessage:String = "",
    val userCollisionExceptionMessage:String = "",

    )