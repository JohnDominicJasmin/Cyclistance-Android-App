package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class IsEmailVerifiedUseCase(private val repository: AuthRepository<AuthCredential>) {
    operator fun invoke():Boolean? = repository.isEmailVerified()
}
