package com.example.cyclistance.feature_message.domain.model.helper

import com.example.cyclistance.feature_message.domain.model.ui.MessageContent

class Conversation {
    private val messages: MutableList<MessageContent> = mutableListOf()

    fun addMessage(message: MessageContent) {
        messages.add(message)
    }

    fun getMessages(): List<MessageContent> {
        return messages
    }

    fun getMessagesBetween(senderId: String, recipientId: String): List<MessageContent> {
        return messages.filter { it.senderId == senderId && it.recipientId == recipientId }
    }
}