package com.example.cyclistance.feature_messaging.domain.use_case.seen

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class MarkAsSeenUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(messageId: String){
        repository.markAsSeen(messageId)
    }
}