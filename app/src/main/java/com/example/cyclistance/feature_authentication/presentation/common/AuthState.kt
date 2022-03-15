package com.example.cyclistance.feature_authentication.presentation.common

data class AuthState <T>(
    val error: String = "",
    val result: T? = null,
    val isLoading: Boolean = false
)