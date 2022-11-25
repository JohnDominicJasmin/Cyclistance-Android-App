package com.example.cyclistance.feature_mapping_screen.domain.websockets

import com.example.cyclistance.core.utils.constants.MappingConstants.BROADCAST_RESCUE_TRANSACTION
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.RescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class RescueTransactionWSClient(
    private val socket: Socket
): WebSocketClient<RescueTransaction> {


    override fun broadCastEvent(t: RescueTransaction?) {
        socket.emit(BROADCAST_RESCUE_TRANSACTION)
    }

    override fun getResult(): Flow<RescueTransaction> {
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
                socket.disconnect();
                socket.off(BROADCAST_RESCUE_TRANSACTION, onNewRescueTransactions);
            }
        }
    }
}