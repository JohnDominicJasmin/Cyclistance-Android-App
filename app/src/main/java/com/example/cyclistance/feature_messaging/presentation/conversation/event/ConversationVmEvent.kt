package com.example.cyclistance.feature_messaging.presentation.conversation.event

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel

sealed class ConversationVmEvent {
    data class SendMessage(val sendMessageModel: SendMessageModel) : ConversationVmEvent()
}
