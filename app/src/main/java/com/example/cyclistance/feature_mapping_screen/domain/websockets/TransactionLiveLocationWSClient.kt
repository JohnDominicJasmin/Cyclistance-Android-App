package com.example.cyclistance.feature_mapping_screen.domain.websockets

import com.example.cyclistance.feature_mapping_screen.domain.model.LiveLocationWSModel
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

            socket.on("broadcasting_location", onNewLocationUpdates)
            socket.connect()

            awaitClose {
                socket.disconnect()
                socket.off("broadcasting_location", onNewLocationUpdates)
            }
        }
    }


    override fun broadCastEvent(t: LiveLocationWSModel?) {
        t?.let{ locationModel ->
            socket.emit("joinLiveLocationUpdates", locationModel.latitude, locationModel.longitude, "room-${locationModel.room}")
        }
    }

}