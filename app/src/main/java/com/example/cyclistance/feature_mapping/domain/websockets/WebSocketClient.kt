package com.example.cyclistance.feature_mapping.domain.websockets

import kotlinx.coroutines.flow.Flow

interface WebSocketClient<T> {
    fun getResult(): Flow<T>
    fun broadCastEvent(t: T? = null)
}