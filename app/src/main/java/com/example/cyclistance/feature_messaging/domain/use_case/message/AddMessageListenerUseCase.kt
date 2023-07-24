package com.example.cyclistance.feature_messaging.domain.use_case.message

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddMessageListenerUseCase(private val repository: MessagingRepository) {

    operator fun invoke(receiverId: String) {
        repository.addMessageListener(receiverId = receiverId)
    }
}