package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.UserMessagesModel

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): UserMessagesModel
    suspend fun sendMessage(sendMessageModel: SendMessageModel): Boolean
    fun deleteToken()
    fun listenForMessages(receiverId: String)
}