package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class CreateUserUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: UserDetails) {
        val userDetails = if(user.name.isEmpty()){
            user.copy(name = user.email.substringBefore("@"))
        }else{
            user
        }
        repository.createUser(user = userDetails)
    }
}