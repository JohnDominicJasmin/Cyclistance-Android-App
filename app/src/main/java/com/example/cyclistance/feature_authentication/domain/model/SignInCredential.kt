package com.example.cyclistance.feature_authentication.domain.model

sealed class SignInCredential(val providerToken: String = "", val providerName: String) {
    class Facebook(providerToken: String) :
        SignInCredential(providerToken = providerToken, providerName = "facebook.com")

    class Google(providerToken: String) :
        SignInCredential(providerToken = providerToken, providerName = "google.com")
}
