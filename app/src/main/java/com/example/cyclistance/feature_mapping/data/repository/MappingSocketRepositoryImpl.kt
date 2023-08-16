package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.api.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MappingSocketRepositoryImpl(
    private val context: Context,
    private val rescueTransactionClient: WebSocketClient<RescueTransaction, LiveLocationWSModel>,
    private val nearbyCyclistClient: WebSocketClient<NearbyCyclist, LiveLocationWSModel>,
    private val liveLocation: WebSocketClient<LiveLocationWSModel, LiveLocationWSModel>,
): MappingSocketRepository {

    private val scope: CoroutineContext = Dispatchers.IO
    override suspend fun getUserUpdates(): Flow<NearbyCyclist> {
        return nearbyCyclistClient.getResult().retry(MappingConstants.API_CALL_RETRY_COUNT) {
            return@retry context.hasInternetConnection().not()
        }
    }

    override suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationWSModel) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        withContext(scope) { nearbyCyclistClient.broadcastEvent(locationModel) }
    }

    override suspend fun broadcastRescueTransactionToRespondent() {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }

        withContext(scope) {
            rescueTransactionClient.broadcastEvent()
        }
    }

    override suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction> {

        return rescueTransactionClient.getResult().retry(MappingConstants.API_CALL_RETRY_COUNT) {
            return@retry context.hasInternetConnection().not()
        }
    }

    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {

        return liveLocation.getResult().retry(MappingConstants.API_CALL_RETRY_COUNT) {
            return@retry context.hasInternetConnection().not()
        }
    }

    override suspend fun broadcastTransactionLocation(locationModel: LiveLocationWSModel) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            liveLocation.broadcastEvent(locationModel)
        }
    }
}