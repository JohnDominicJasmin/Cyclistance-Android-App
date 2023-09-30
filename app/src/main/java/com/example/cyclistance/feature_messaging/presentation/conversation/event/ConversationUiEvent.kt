package com.example.cyclistance.feature_messaging.presentation.conversation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConversationUiEvent {
    data object ToggleMessageArea : ConversationUiEvent()
    data object ResetSelectedIndex : ConversationUiEvent()
    data object OnSendMessage : ConversationUiEvent()
    data class SelectChatItem(val index: Int) : ConversationUiEvent()
    data class OnChangeValueMessage(val message: TextFieldValue) : ConversationUiEvent()
    data object CloseConversationScreen : ConversationUiEvent()
    data object DismissNotificationPermissionDialog : ConversationUiEvent()
    data class ResendDialogVisibility(val visible: Boolean) : ConversationUiEvent()
    data class ResendMessage(val message: String) : ConversationUiEvent()


}