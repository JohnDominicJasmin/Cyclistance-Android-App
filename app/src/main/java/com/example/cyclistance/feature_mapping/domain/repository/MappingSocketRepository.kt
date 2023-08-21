package com.example.cyclistance.feature_mapping.domain.repository

import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import kotlinx.coroutines.flow.Flow

interface MappingSocketRepository {

    suspend fun getUserUpdates(): Flow<NearbyCyclist>
    suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    suspend fun getTransactionLocationUpdates(): Flow<LiveLocationSocketModel>
    suspend fun getNewHazardousLaneUpdates(): Flow<HazardousLane>

    suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationSocketModel)
    suspend fun broadcastRescueTransactionToRespondent()
    suspend fun addNewHazardousLane(hazardousLaneMarker: HazardousLaneMarker)
    suspend fun requestHazardousLane()
    suspend fun deleteHazardousLane(id: String)
    suspend fun broadcastTransactionLocation(locationModel: LiveLocationSocketModel)
}