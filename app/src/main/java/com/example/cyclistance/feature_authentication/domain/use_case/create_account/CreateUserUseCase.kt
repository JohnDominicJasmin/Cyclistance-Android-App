package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class CreateUserUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: UserDetails) {
        repository.createUser(user = user)
    }
}