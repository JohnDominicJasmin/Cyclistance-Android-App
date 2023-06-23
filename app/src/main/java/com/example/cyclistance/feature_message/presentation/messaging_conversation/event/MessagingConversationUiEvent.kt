package com.example.cyclistance.feature_message.presentation.messaging_conversation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class MessagingConversationUiEvent {
    object ToggleMessageArea : MessagingConversationUiEvent()
    object ResetSelectedIndex : MessagingConversationUiEvent()
    data class SelectChatItem(val index: Int) : MessagingConversationUiEvent()
    data class ChangeMessage(val message: TextFieldValue) : MessagingConversationUiEvent()

}
