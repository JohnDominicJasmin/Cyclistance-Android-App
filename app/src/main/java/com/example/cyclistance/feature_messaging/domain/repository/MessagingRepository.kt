package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUsers

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): MessagingUsers
}