package com.example.cyclistance.feature_message.presentation.messaging_conversation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class MessagingConversationUiEvent {
    object ToggleMessageArea : MessagingConversationUiEvent()
    object ResetSelectedIndex : MessagingConversationUiEvent()
    object CloseMessagingConversationScreen : MessagingConversationUiEvent()
    data class SelectChatItem(val index: Int) : MessagingConversationUiEvent()
    data class OnChangeMessage(val message: TextFieldValue) : MessagingConversationUiEvent()

}
