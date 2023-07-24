package com.example.cyclistance.feature_messaging.presentation.chat.chats.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel

sealed class MessagingUiEvent {
    object CloseScreen : MessagingUiEvent()
    data class OnSelectConversation(val messageItem: ChatItemModel) : MessagingUiEvent()
    data class OnSearchQueryChanged(val searchQuery: TextFieldValue) : MessagingUiEvent()
    object OnClickSearch : MessagingUiEvent()
    object ClearSearchQuery : MessagingUiEvent()
    object DismissSearchMessageDialog : MessagingUiEvent()

}
