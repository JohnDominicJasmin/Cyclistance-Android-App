package com.myapp.cyclistance.feature_messaging.domain.use_case.conversion

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddConversionUseCase(private val repository: MessagingRepository) {
    operator fun invoke(receiverId: String, message: String, onNewConversionId: (String) -> Unit) =
        repository.addConversion(receiverId = receiverId, message = message, onNewConversionId)

}