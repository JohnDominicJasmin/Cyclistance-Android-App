package com.example.cyclistance.feature_messaging.domain.use_case.manage_user

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddUserListenerUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(): MessagingUserModel {
        return repository.addUserListener()
    }
}