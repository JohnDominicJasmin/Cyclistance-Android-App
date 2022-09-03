package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.first

class GetPhoneNumberUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(): String? {
        return repository.getPhoneNumber().first()
    }
}