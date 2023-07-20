package com.example.cyclistance.feature_messaging.domain.repository

interface MessagingRepository {

    suspend fun refreshToken()
}