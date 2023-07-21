package com.example.cyclistance.feature_messaging.domain.use_case.manage_user

import com.example.cyclistance.feature_messaging.domain.model.MessagingUsers
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetUsersUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(): MessagingUsers {
        return repository.getUsers()
    }
}