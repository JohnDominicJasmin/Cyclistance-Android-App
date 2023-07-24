package com.example.cyclistance.feature_messaging.domain.use_case.manage_user

import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.UserMessagesModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetUsersUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(): UserMessagesModel {
        return repository.getUsers()
    }
}