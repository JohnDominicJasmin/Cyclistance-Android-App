package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLane
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketClient
import com.example.cyclistance.feature_mapping.domain.sockets.WebSocketResultSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MappingSocketRepositoryImpl(
    private val context: Context,
    private val nearbyCyclistClient: WebSocketClientSender<LiveLocationSocketModel>, WebSocketClientReceiver<NearbyCyclist>,
    private val rescueTransactionClient: WebSocketClient<RescueTransaction, LiveLocationSocketModel>,
    private val liveLocation: WebSocketClient<LiveLocationSocketModel, LiveLocationSocketModel>,
    private val addHazardousLaneClient: WebSocketClient<HazardousLane, HazardousLaneMarker>,
    private val deleteHazardousLane: WebSocketClient<HazardousLane, String>


    ) : MappingSocketRepository {


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

    override suspend fun getNewHazardousLaneUpdates(): Flow<HazardousLane> {

        return addHazardousLaneClient.getResult().retry(MappingConstants.API_CALL_RETRY_COUNT) {
            return@retry context.hasInternetConnection().not()
        }
    }

    override suspend fun addNewHazardousLane(hazardousLaneMarker: HazardousLaneMarker) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            addHazardousLaneClient.broadcastEvent(hazardousLaneMarker)
        }
    }

    override suspend fun deleteHazardousLane(id: String) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            deleteHazardousLane.broadcastEvent(id)
        }
    }

    override suspend fun requestHazardousLane() {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            requestHazardousLaneClient.broadcastEvent()
        }
    }
}