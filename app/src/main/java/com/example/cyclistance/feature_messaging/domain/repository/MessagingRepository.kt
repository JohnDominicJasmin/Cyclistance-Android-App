package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUsers

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): MessagingUsers
    suspend fun sendMessage(conversationItem: ConversationItemModel): Boolean
    fun deleteToken()
}