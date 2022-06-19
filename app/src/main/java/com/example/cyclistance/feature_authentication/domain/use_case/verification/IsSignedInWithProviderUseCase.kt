package com.example.cyclistance.feature_authentication.domain.use_case.verification

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.*

class IsSignedInWithProviderUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(): Boolean? {
        return repository.isSignedInWithProvider().firstOrNull { it }
    }


}