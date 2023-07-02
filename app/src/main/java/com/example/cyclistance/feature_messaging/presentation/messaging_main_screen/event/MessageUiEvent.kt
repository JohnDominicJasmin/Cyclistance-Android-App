package com.example.cyclistance.feature_messaging.presentation.messaging_main_screen.event

sealed class MessageUiEvent {
    data class OnMessageClicked(val messageId: String) : MessageUiEvent()
}
