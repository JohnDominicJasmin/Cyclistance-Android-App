package com.example.cyclistance.feature_messaging.presentation.chats.event

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel

sealed class MessagingVmEvent {
    data class SendMessage(val sendMessageModel: SendMessageModel) : MessagingVmEvent()

}
