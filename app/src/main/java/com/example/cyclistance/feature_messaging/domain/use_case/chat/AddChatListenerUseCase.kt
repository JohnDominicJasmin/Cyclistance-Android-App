package com.example.cyclistance.feature_messaging.domain.use_case.chat

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class AddChatListenerUseCase(private val repository: MessagingRepository) {
    operator fun invoke(
        onAddedChat: (ChatItemModel) -> Unit,
        onModifiedChat: (ChatItemModel) -> Unit) {
        repository.addChatListener(onAddedChat = onAddedChat, onModifiedChat = onModifiedChat)
    }
}