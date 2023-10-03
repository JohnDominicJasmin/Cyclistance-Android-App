package com.example.cyclistance.feature_messaging.domain.use_case.conversion

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class UpdateConversionUseCase(private val repository: MessagingRepository) {
    operator fun invoke(message: String, conversionId: String, receiverId: String){
        repository.updateConversion(message = message, conversionId = conversionId, receiverId = receiverId)
    }
}