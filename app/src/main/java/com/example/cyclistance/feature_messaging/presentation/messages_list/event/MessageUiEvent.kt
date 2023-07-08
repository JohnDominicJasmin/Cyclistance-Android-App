package com.example.cyclistance.feature_messaging.presentation.messages_list.event

sealed class MessageUiEvent {
    data class OnMessageClicked(val messageId: String) : MessageUiEvent()
}
