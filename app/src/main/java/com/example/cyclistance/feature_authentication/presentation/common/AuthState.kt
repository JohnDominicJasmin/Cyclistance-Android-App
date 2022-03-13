package com.example.cyclistance.feature_authentication.presentation.common

data class AuthState(
    val error: String = "",
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false
)