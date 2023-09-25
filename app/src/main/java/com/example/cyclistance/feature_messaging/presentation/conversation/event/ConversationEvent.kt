package com.example.cyclistance.feature_messaging.presentation.conversation.event

sealed class ConversationEvent {
    data object MessageSent : ConversationEvent()
    data object MessageNotSent : ConversationEvent()

}
