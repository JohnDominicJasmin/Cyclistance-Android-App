package com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account

import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository

class IsSignedInWithProviderUseCase(private val repository: AuthRepository) {

     operator fun invoke(): Boolean? {
        return repository.isSignedInWithProvider()
    }


}