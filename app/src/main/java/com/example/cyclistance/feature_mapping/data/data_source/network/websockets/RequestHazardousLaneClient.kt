package com.example.cyclistance.feature_mapping.data.data_source.network.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketResultSender
import io.socket.client.Socket

class RequestHazardousLaneClient(private val socket: Socket) :
    WebSocketResultSender<Any> {

    override suspend fun broadcastEvent(broadcastItem: Any?) {
        socket.emit(MappingConstants.GET_ALL_HAZARDOUS_LANE)
    }
}