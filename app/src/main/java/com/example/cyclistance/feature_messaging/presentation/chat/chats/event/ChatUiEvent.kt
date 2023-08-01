package com.example.cyclistance.feature_messaging.presentation.chat.chats.event

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel

sealed class ChatUiEvent {
    data class OnSelectConversation(val chatItem: ChatItemModel) : ChatUiEvent()
    object OnRefreshChat: ChatUiEvent()
}
