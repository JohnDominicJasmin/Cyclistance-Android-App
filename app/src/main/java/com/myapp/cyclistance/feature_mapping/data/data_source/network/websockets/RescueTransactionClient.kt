package com.myapp.cyclistance.feature_mapping.data.data_source.network.websockets

import com.google.gson.Gson
import com.myapp.cyclistance.core.utils.constants.MappingConstants.BROADCAST_RESCUE_TRANSACTION
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RescueTransactionDto
import com.myapp.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.sockets.WebSocketClient
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class RescueTransactionClient(
    private val socket: Socket
): WebSocketClient<RescueTransaction, LiveLocationSocketModel> {


    override suspend fun broadcastEvent(broadcastItem: LiveLocationSocketModel?) {
        socket.emit(BROADCAST_RESCUE_TRANSACTION)
    }

    override suspend  fun getResult(): Flow<RescueTransaction> {
        return callbackFlow {

            val gson = Gson()
            val onNewRescueTransactions =
                Emitter.Listener { response ->
                    val responseResult = response[0].toString().trimIndent()
                    val result = gson.fromJson(responseResult, RescueTransactionDto::class.java)
                    trySend(result.toRescueTransaction())
                }


            socket.on(BROADCAST_RESCUE_TRANSACTION, onNewRescueTransactions)
            socket.connect()

            awaitClose {
                socket.disconnect()
                socket.off(BROADCAST_RESCUE_TRANSACTION, onNewRescueTransactions)
            }
        }
    }
}