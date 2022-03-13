package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class IsSignedInWithProviderUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(id: String): Flow<Boolean> = repository.isSignedInWithProvider(id)

}