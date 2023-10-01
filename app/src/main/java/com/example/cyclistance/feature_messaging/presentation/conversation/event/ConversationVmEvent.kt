package com.example.cyclistance.feature_messaging.presentation.conversation.event

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class ConversationVmEvent {
    data class SendMessage(val sendMessageModel: SendMessageModel) : ConversationVmEvent()
    data class OnInitialized(
        val userReceiverMessage: MessagingUserItemModel,
        val userSenderMessage: MessagingUserItemModel) : ConversationVmEvent()

    data object ResendMessage : ConversationVmEvent()
    data class MarkAsSeen(val messageId: String) : ConversationVmEvent()
}
