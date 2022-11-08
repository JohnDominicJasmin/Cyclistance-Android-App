package com.example.cyclistance.core.utils.websockets

import kotlinx.coroutines.flow.Flow

interface WebSocketClient<T> {
    fun getResult(): Flow<T>
    fun broadCastEvent()
}