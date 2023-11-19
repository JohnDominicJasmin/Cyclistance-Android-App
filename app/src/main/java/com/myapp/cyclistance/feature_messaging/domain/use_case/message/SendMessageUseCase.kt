package com.myapp.cyclistance.feature_messaging.domain.use_case.message

import com.myapp.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class SendMessageUseCase(private val repository: MessagingRepository) {

    suspend operator fun invoke(sendMessageModel: SendMessageModel) {
        repository.sendMessage(sendMessageModel = sendMessageModel)
    }

}