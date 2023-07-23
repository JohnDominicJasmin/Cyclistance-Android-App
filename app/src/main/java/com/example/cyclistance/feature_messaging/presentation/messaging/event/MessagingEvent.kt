package com.example.cyclistance.feature_messaging.presentation.messaging.event

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel

sealed class MessagingEvent {
    data class SendMessage(val sendMessageModel: SendMessageModel) : MessagingEvent()
    data class AddMessageListener(val receiverId: String) : MessagingEvent()
    object RemoveMessageListener : MessagingEvent()
}
