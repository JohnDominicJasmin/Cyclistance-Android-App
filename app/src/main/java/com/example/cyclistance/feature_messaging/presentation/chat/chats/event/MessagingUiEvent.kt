package com.example.cyclistance.feature_messaging.presentation.chat.chats.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class MessagingUiEvent {
    object CloseScreen : MessagingUiEvent()
    data class OnSelectConversation(val name: String, val id: String, val photoUrl: String) :
        MessagingUiEvent()

    data class OnSearchQueryChanged(val searchQuery: TextFieldValue) : MessagingUiEvent()
    object OnClickSearch : MessagingUiEvent()
    object ClearSearchQuery : MessagingUiEvent()
    object DismissSearchMessageDialog : MessagingUiEvent()

}
