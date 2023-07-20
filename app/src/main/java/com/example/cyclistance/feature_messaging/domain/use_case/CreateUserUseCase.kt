package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class CreateUserUseCase(private val repository: MessagingRepository) {

    suspend operator fun invoke(authUser: UserDetails) {
        repository.createUser(authUser)
    }

}