package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import com.example.cyclistance.feature_authentication.presentation.common.AuthState

data class SignInState<T>(
    val authState: AuthState<T>? = null,
    val emailExceptionMessage:String = "",
    val passwordExceptionMessage:String = "",
    val internetExceptionMessage:String = "",
    val invalidUserExceptionMessage:String = ""
)
