package com.example.cyclistance.feature_mapping.domain.websockets

import kotlinx.coroutines.flow.Flow

interface WebSocketClient<T> {
    suspend fun getResult(): Flow<T>
    suspend fun broadCastEvent(t: T? = null)
}