package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): MessagingUserModel
    fun sendMessage(sendMessageModel: SendMessageModel)
    suspend fun deleteToken()
    fun addMessageListener(receiverId: String)
    fun removeMessageListener()
}