package com.example.cyclistance.feature_messaging.domain.model.helper

import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel

class Conversation {
    private val messages: MutableList<ConversationItemModel> = mutableListOf()

    fun addMessage(message: ConversationItemModel) {
        messages.add(message)
    }

    fun getMessages(): List<ConversationItemModel> {
        return messages
    }

    fun getMessagesBetween(senderId: String, recipientId: String): List<ConversationItemModel> {
        return messages.filter { it.senderId == senderId && it.receiverId == recipientId }
    }
}