package com.example.cyclistance.feature_mapping.domain.websockets

import kotlinx.coroutines.flow.Flow

interface WebSocketClient<out T, in V : Any?> {
    suspend fun getResult(): Flow<T>
    suspend fun broadcastEvent(t: V? = null)
}