package com.myapp.cyclistance.feature_mapping.domain.sockets

interface WebSocketClient<out T, in V : Any?> : WebSocketResultReceiver<T>,
    WebSocketResultSender<V>

