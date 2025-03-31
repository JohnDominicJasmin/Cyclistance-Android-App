package com.myapp.cyclistance.feature_mapping.domain.repository

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import kotlinx.coroutines.flow.Flow

interface MappingSocketRepository {

    suspend fun getUserUpdates(): Flow<NearbyCyclist>
    suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    suspend fun getTransactionLocationUpdates(): Flow<LiveLocationSocketModel>


    suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationSocketModel)
    suspend fun broadcastRescueTransactionToRespondent()

    suspend fun broadcastTransactionLocation(locationModel: LiveLocationSocketModel)
}