package com.example.cyclistance.feature_messaging.presentation.messaging.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessageItemModel

sealed class MessagingUiEvent {
    object ToggleMessageArea : MessagingUiEvent()
    object ResetSelectedIndex : MessagingUiEvent()
    object DismissConversationDialog : MessagingUiEvent()
    data class SelectChatItem(val index: Int) : MessagingUiEvent()
    data class OnChangeMessage(val message: TextFieldValue) : MessagingUiEvent()
    data class OnSelectedConversation(val messageItem: MessageItemModel) : MessagingUiEvent()
}
