package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class ReloadEmailUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke():Boolean = repository.reloadEmail()

}