package com.example.cyclistance.feature_authentication.domain.model

sealed class SignInCredential(val token:String = ""){
    class Facebook(token:String): SignInCredential(token)
    class Google(token:String): SignInCredential(token)
}
