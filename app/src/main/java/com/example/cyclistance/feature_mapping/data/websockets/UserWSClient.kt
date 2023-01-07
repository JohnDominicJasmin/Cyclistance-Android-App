package com.example.cyclistance.feature_mapping.data.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants.BROADCAST_USERS
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping.domain.model.User
import com.example.cyclistance.feature_mapping.domain.websockets.WebSocketClient
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserWSClient(
    private val socket: Socket
): WebSocketClient<User> {


    override suspend  fun broadCastEvent(t: User?) {
        socket.emit(BROADCAST_USERS)
    }

    override suspend fun getResult(): Flow<User> {
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
                socket.disconnect();
                socket.off(BROADCAST_USERS, onNewUsers)
            }
        }
    }


}