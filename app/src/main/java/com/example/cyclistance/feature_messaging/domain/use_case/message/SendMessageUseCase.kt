package com.example.cyclistance.feature_messaging.domain.use_case.message

import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class SendMessageUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(conversationItem: ConversationItemModel): Boolean {
        return repository.sendMessage(conversationItem = conversationItem)
    }
}