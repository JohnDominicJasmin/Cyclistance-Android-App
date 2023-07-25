package com.example.cyclistance.feature_messaging.presentation.conversation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConversationUiEvent {
    object ToggleMessageArea : ConversationUiEvent()
    object ResetSelectedIndex : ConversationUiEvent()
    object OnSendMessage : ConversationUiEvent()
    data class SelectChatItem(val index: Int) : ConversationUiEvent()
    data class OnChangeValueMessage(val message: TextFieldValue) : ConversationUiEvent()
    object CloseConversationScreen : ConversationUiEvent()

}