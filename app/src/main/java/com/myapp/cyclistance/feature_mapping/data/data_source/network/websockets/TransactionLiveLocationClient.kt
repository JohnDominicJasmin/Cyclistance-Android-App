package com.myapp.cyclistance.feature_mapping.data.data_source.network.websockets

import com.google.gson.Gson
import com.myapp.cyclistance.core.utils.constants.MappingConstants.BROADCAST_LOCATION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.JOIN_LIVE_LOCATION_UPDATES
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.sockets.WebSocketClient
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TransactionLiveLocationClient(
    private val socket: Socket
): WebSocketClient<LiveLocationSocketModel, LiveLocationSocketModel> {
    
    override suspend fun getResult(): Flow<LiveLocationSocketModel> {
        return callbackFlow {
            val onNewLocationUpdates = Emitter.Listener { response ->
                val gson = Gson()
                val responseResult = response[0].toString().trimIndent()
                val result = gson.fromJson(responseResult, LiveLocationSocketModel::class.java)
                trySend(result)
            }

            socket.on(BROADCAST_LOCATION, onNewLocationUpdates)
            socket.connect()

            awaitClose {
                socket.disconnect()
                socket.off(BROADCAST_LOCATION, onNewLocationUpdates)
            }
        }
    }


    override suspend fun broadcastEvent(broadcastItem: LiveLocationSocketModel?) {
        broadcastItem?.let{ locationModel ->
            socket.emit(JOIN_LIVE_LOCATION_UPDATES, locationModel.latitude, locationModel.longitude, "room-${locationModel.room}")
        }
    }

}