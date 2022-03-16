package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile

class IsSignedInWithProviderUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(): Boolean {
        return repository.isSignedInWithProvider().takeWhile { it }.first()

    }


}