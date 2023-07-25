package com.example.cyclistance.feature_messaging.presentation.chats.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel

sealed class MessagingUiEvent {
    object CloseMessagingScreen : MessagingUiEvent()
    data class OnSelectConversation(val messageItem: ChatItemModel) : MessagingUiEvent()
    data class OnSearchQueryChanged(val searchQuery: TextFieldValue) : MessagingUiEvent()
    object OnClickSearch : MessagingUiEvent()
    object ClearSearchQuery : MessagingUiEvent()

}
