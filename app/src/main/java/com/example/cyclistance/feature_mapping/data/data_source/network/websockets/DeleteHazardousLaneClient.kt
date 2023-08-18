package com.example.cyclistance.feature_mapping.data.data_source.network.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants.DELETE_HAZARDOUS_LANE
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketResultSender
import io.socket.client.Socket

class DeleteHazardousLaneClient(
    private val socket: Socket
): WebSocketResultSender<String> {


    override suspend fun broadcastEvent(broadcastItem: String?) {
        broadcastItem?.let { hazardousId ->
            socket.emit(DELETE_HAZARDOUS_LANE, hazardousId)
        }
    }
}