package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class SignInWithCredentialUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(authCredential: SignInCredential) =
        repository.signInWithCredential(authCredential)
}