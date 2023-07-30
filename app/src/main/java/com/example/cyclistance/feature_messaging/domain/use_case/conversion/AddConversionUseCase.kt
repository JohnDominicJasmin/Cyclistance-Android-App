package com.example.cyclistance.feature_messaging.domain.use_case.conversion

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddConversionUseCase(private val repository: MessagingRepository) {
    operator fun invoke(conversion: HashMap<String, Any>, onNewConversionId: (String) -> Unit) =
        repository.addConversion(conversion, onNewConversionId)

}