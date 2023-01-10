package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class ReloadEmailUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke():Boolean = repository.reloadEmail()

}