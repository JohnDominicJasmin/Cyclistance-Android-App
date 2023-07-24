package com.example.cyclistance.feature_messaging.presentation.chats.event

import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel

sealed class MessagingUiEvent {
    object CloseMessagingScreen : MessagingUiEvent()
    data class OnSelectConversation(val messageItem: ChatItemModel) : MessagingUiEvent()
}
