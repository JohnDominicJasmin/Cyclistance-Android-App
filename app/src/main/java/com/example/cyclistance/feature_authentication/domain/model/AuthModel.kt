package com.example.cyclistance.feature_authentication.domain.model


data class AuthModel(
    val email:String = "",
    val password:String = "",
    val confirmPassword:String = "",

)