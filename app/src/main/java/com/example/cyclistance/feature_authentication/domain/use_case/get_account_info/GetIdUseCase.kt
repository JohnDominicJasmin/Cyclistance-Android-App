package com.example.cyclistance.feature_authentication.domain.use_case.get_account_info

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetIdUseCase(private val repository: AuthRepository<AuthCredential>) {
    operator fun invoke():String? {

        return repository.getId()

    }
}