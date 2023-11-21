package com.myapp.cyclistance.feature_mapping.domain.sockets

interface WebSocketResultSender<in V : Any?> {
    suspend fun broadcastEvent(broadcastItem: V? = null)
}