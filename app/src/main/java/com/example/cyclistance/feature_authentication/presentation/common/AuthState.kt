package com.example.cyclistance.feature_authentication.presentation.common

import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions

data class AuthState <T>(
    val emailExceptionMessage: String = "",
    val passwordExceptionMessage: String = "",
    val confirmPasswordExceptionMessage: String = "",
    val internetExceptionMessage: String = "",
    val invalidUserExceptionMessage: String = "",
    val userCollisionExceptionMessage: String = "",
    val conflictFBTokenExceptionMessage: String = "",
    val sendEmailExceptionMessage: String = "",
    var result: T? = null,
    val isLoading: Boolean = false,
)

