package com.example.cyclistance.feature_mapping.domain.repository

import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.model.*
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): UserItem
    suspend fun getUsers(): NearbyCyclist
    suspend fun createUser(userItem: UserItem)
    suspend fun deleteUser(id: String)


    suspend fun deleteRescueRespondent(userId: String, respondentId: String)
    suspend fun addRescueRespondent(userId: String, respondentId: String)
    suspend fun deleteAllRespondents(userId: String)


    suspend fun getFullAddress(latitude: Double, longitude: Double):String
    suspend fun getRescueTransactionById(transactionId: String): RescueTransactionItem
    suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem)
    suspend fun deleteRescueTransaction(transactionId: String)


    suspend fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    suspend fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

    suspend fun getUserLocation(): Flow<Location>


    suspend fun getUserUpdates():Flow<NearbyCyclist>
    suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel>
    suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationWSModel)
    suspend fun broadcastRescueTransactionToRespondent()
    suspend fun broadcastTransactionLocation(locationModel: LiveLocationWSModel)



    suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection
    fun getCalculateDistance(startingLocation: Location, destinationLocation: Location):Double

}