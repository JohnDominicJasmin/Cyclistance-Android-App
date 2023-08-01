package com.example.cyclistance.feature_messaging.domain.use_case.chat

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class RemoveChatListenerUseCase(private val repository: MessagingRepository) {
    operator fun invoke(){
        repository.removeChatListener()
    }
}