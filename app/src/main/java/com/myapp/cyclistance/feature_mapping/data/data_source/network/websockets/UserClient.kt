package com.myapp.cyclistance.feature_mapping.data.data_source.network.websockets

import com.google.gson.Gson
import com.myapp.cyclistance.core.utils.constants.MappingConstants.BROADCAST_USERS
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserDto
import com.myapp.cyclistance.feature_mapping.data.mapper.UserMapper.toUser
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.myapp.cyclistance.feature_mapping.domain.sockets.WebSocketClient
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserClient(
    private val socket: Socket
): WebSocketClient<NearbyCyclist, LiveLocationSocketModel> {


    override suspend fun broadcastEvent(t: LiveLocationSocketModel?) {
        t?.let{ locationModel ->
            socket.emit(BROADCAST_USERS, locationModel.latitude, locationModel.longitude)
        }
    }

    override suspend fun getResult(): Flow<NearbyCyclist> {
        return callbackFlow {
            val gson = Gson()
            val onNewUsers = Emitter.Listener { response ->
                val responseResult = response[0].toString().trimIndent()
                val result = gson.fromJson(responseResult, UserDto::class.java)
                trySend(result.toUser())
            }


            socket.on(BROADCAST_USERS, onNewUsers)
            socket.connect()

            awaitClose {
                socket.disconnect()
                socket.off(BROADCAST_USERS, onNewUsers)
            }
        }
    }


}