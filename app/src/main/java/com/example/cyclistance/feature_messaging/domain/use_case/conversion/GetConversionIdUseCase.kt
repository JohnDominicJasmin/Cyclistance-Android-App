package com.example.cyclistance.feature_messaging.domain.use_case.conversion

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetConversionIdUseCase(private val repository: MessagingRepository) {

    suspend operator fun invoke(receiverId: String): String {
        return repository.getConversionId(receiverId = receiverId)
    }
}