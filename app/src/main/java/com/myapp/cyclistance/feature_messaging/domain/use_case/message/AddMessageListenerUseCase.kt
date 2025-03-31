package com.myapp.cyclistance.feature_messaging.domain.use_case.message

import com.myapp.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddMessageListenerUseCase(private val repository: MessagingRepository) {

    operator fun invoke(receiverId: String, onNewMessage: (ConversationsModel) -> Unit) {
        repository.addMessageListener(receiverId = receiverId, onNewMessage = onNewMessage)
    }
}