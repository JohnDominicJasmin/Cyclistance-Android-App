package com.example.cyclistance.feature_mapping.data.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants.BROADCAST_LOCATION
import com.example.cyclistance.core.utils.constants.MappingConstants.JOIN_LIVE_LOCATION_UPDATES
import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.websockets.WebSocketClient
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TransactionLiveLocationWSClient(
    private val socket: Socket
): WebSocketClient<LiveLocationWSModel> {
    
    override fun getResult(): Flow<LiveLocationWSModel> {
        return callbackFlow {
            val onNewLocationUpdates = Emitter.Listener { response ->
                val gson = Gson()
                val responseResult = response[0].toString().trimIndent()
                val result = gson.fromJson(responseResult, LiveLocationWSModel::class.java)
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


    override fun broadCastEvent(t: LiveLocationWSModel?) {
        t?.let{ locationModel ->
            socket.emit(JOIN_LIVE_LOCATION_UPDATES, locationModel.latitude, locationModel.longitude, "room-${locationModel.room}")
        }
    }

}