package com.myapp.cyclistance.feature_messaging.presentation.conversation.event

import com.myapp.cyclistance.feature_messaging.domain.model.SendMessageModel

sealed class ConversationVmEvent {
    data class SendMessage(val sendMessageModel: SendMessageModel) : ConversationVmEvent()
    data class OnInitialized(val userReceiverId: String) : ConversationVmEvent()

    data object ResendMessage : ConversationVmEvent()
    data class MarkAsSeen(val messageId: String) : ConversationVmEvent()
}
