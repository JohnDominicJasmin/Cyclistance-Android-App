package com.example.cyclistance.feature_messaging.presentation.conversation.event

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class ConversationEvent {
    data class ResendMessageFailed(val message: String) : ConversationEvent()
    data class LoadConversationSuccess(val message: MessagingUserItemModel) : ConversationEvent()
}
