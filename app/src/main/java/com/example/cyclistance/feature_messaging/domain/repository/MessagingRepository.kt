package com.example.cyclistance.feature_messaging.domain.repository

import com.example.cyclistance.feature_messaging.domain.model.MessagingUsers

interface MessagingRepository {

    suspend fun refreshToken()
    suspend fun getUsers(): MessagingUsers
}