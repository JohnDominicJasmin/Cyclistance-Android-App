package com.example.cyclistance.feature_mapping.domain.repository

import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.api.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import kotlinx.coroutines.flow.Flow

interface MappingSocketRepository {

    suspend fun getUserUpdates(): Flow<NearbyCyclist>
    suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel>
    suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationWSModel)
    suspend fun broadcastRescueTransactionToRespondent()
    suspend fun broadcastTransactionLocation(locationModel: LiveLocationWSModel)
}