package com.example.cyclistance.feature_mapping.data.data_source.network.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants.NEW_HAZARDOUS_LANE
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.hazardous_lane.HazardousLaneDto
import com.example.cyclistance.feature_mapping.data.mapper.HazardousLaneMapper.toHazardousLane
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLane
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketClientReceiver
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketClientSender
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class AddHazardousLaneClient(
    private val socket: Socket
) : WebSocketClientReceiver<HazardousLane>, WebSocketClientSender<HazardousLaneMarker> {

    override suspend fun getResult(): Flow<HazardousLane> {
        return callbackFlow {
            val gson = Gson()
            val onNewHazardousLane = Emitter.Listener { response ->

                val responseResult = response[0].toString().trimIndent()
                val result = gson.fromJson(responseResult, HazardousLaneDto::class.java)
                trySend(element = result.toHazardousLane())

            }

            socket.on(NEW_HAZARDOUS_LANE, onNewHazardousLane)
            socket.connect()


            awaitClose {
                socket.disconnect()
                socket.off(NEW_HAZARDOUS_LANE, onNewHazardousLane)
            }

        }
    }

    override suspend fun broadcastEvent(broadcastItem: HazardousLaneMarker?) {
        broadcastItem?.let { marker ->
            val gson = Gson()
            socket.emit(NEW_HAZARDOUS_LANE, gson.toJson(marker))
        }
    }
}