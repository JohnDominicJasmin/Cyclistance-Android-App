package com.myapp.cyclistance.feature_messaging.domain.use_case.seen

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class MarkAsSeenUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(messageId: String, conversionId: String){
        repository.markAsSeen(messageId, conversionId)
    }
}