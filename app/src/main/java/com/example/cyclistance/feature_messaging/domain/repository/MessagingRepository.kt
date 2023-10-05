package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel

interface MessagingRepository {

    suspend fun refreshToken()

    suspend fun deleteToken()
    fun getUserUid(): String

    suspend fun reEnableNetworkSync()


    suspend fun sendMessage(sendMessageModel: SendMessageModel)
    fun addMessageListener(receiverId: String, onNewMessage: (ConversationsModel) -> Unit)
    fun removeMessageListener()


    suspend fun getConversionId(receiverId: String): String
    fun addConversion(receiverId: String, message: String,  onNewConversionId: (String) -> Unit)
    fun updateConversion(message: String, conversionId: String, receiverId: String,)

    fun updateUserAvailability(isUserAvailable: Boolean)

    suspend fun sendNotification(model : SendNotificationModel)

    suspend fun getMessagingUser(uid: String): MessagingUserItemModel

    suspend fun markAsSeen(messageId: String, conversionId: String)

}