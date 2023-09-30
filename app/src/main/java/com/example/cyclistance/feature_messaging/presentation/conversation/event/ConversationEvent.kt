package com.example.cyclistance.feature_messaging.presentation.conversation.event

sealed class ConversationEvent {
    data class ResendMessageFailed(val message: String) : ConversationEvent()

}
