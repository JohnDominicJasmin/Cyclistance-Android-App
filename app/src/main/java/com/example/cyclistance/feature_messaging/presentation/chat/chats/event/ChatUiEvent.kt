package com.example.cyclistance.feature_messaging.presentation.chat.chats.event

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class ChatUiEvent {
    data class OnSelectConversation(val user: MessagingUserItemModel) : ChatUiEvent()
    object OnRefreshChat: ChatUiEvent()
}
