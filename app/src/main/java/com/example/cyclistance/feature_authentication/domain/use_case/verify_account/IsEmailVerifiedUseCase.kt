package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class IsEmailVerifiedUseCase(private val repository: AuthRepository) {
    operator fun invoke():Boolean? = repository.isEmailVerified()
}
