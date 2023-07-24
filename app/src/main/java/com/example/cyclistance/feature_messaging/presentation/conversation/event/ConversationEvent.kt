package com.example.cyclistance.feature_messaging.presentation.conversation.event

sealed class ConversationEvent {
    object MessageSent : ConversationEvent()
    object MessageNotSent : ConversationEvent()

}
