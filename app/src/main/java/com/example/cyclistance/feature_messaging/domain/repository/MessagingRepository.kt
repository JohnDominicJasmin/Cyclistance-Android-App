package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel

interface MessagingRepository {

    suspend fun refreshToken()

    suspend fun deleteToken()
    fun getUserUid(): String

    fun addUserListener(onNewMessageUser: (MessagingUserModel) -> Unit)
    fun removeUserListener()


    suspend fun sendMessage(sendMessageModel: SendMessageModel)
    fun addMessageListener(receiverId: String, onNewMessage: (ConversationsModel) -> Unit)
    fun removeMessageListener()

    fun addChatListener(onAddedChat: (ChatItemModel) -> Unit, onModifiedChat: (ChatItemModel) -> Unit)
    fun removeChatListener()


    suspend fun getConversionId(receiverId: String): String
    fun addConversion(conversion: HashMap<String, Any>, onNewConversionId: (String) -> Unit)
    fun updateConversion(message: String, conversionId: String)

    fun updateUserAvailability(isUserAvailable: Boolean)

    suspend fun sendNotification(model : SendNotificationModel)

    suspend fun getMessagingUser(uid: String): MessagingUserItemModel
}