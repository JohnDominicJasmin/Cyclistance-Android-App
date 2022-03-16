package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import com.example.cyclistance.feature_authentication.presentation.common.AuthState

data class SignUpState<T>(

    val authState: AuthState<T>? = null,
    val emailExceptionMessage:String = "",
    val passwordExceptionMessage:String = "",
    val confirmPasswordExceptionMessage:String = "",
    val internetExceptionMessage:String = "",
    val invalidUserExceptionMessage:String = ""

)