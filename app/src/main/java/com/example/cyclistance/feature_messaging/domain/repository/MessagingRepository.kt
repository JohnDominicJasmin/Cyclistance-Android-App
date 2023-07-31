package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatsModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): MessagingUserModel
    fun sendMessage(sendMessageModel: SendMessageModel)
    suspend fun deleteToken()
    fun getUserUid(): String
    fun addMessageListener(receiverId: String, onNewMessage: (ConversationsModel) -> Unit)
    fun removeMessageListener()

    fun addChatListener(receiverId: String, onNewChat: (ChatsModel) -> Unit)
    fun removeChatListener()
    suspend fun getConversionId(receiverId: String): String
    fun addConversion(conversion: HashMap<String, Any>, onNewConversionId: (String) -> Unit)
    fun updateConversion(message: String, conversionId: String)
}