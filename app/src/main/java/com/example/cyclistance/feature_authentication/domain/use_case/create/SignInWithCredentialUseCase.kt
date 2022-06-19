package com.example.cyclistance.feature_authentication.domain.use_case.create

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class SignInWithCredentialUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(authCredential: AuthCredential) =
        repository.signInWithCredentials(authCredential)
}