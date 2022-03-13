package com.example.cyclistance.feature_authentication.domain.model

import com.google.firebase.auth.AuthCredential

data class AuthInputModel(
    val email:String = "",
    val password:String = "",
    val confirmPassword:String = "",
    val authCredential: AuthCredential,
    val providerId:String = ""
)