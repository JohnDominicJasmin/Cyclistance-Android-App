package com.myapp.cyclistance.feature_messaging.domain.use_case.message

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class RemoveMessageListenerUseCase(private val repository: MessagingRepository) {
    operator fun invoke() {
        repository.removeMessageListener()
    }
}