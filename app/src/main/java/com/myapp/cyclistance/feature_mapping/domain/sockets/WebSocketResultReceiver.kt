package com.myapp.cyclistance.feature_mapping.domain.sockets

import kotlinx.coroutines.flow.Flow

interface WebSocketResultReceiver<out T> {
    suspend fun getResult(): Flow<T>
}