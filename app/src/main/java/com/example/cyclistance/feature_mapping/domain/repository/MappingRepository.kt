package com.example.cyclistance.feature_mapping.domain.repository

import android.graphics.drawable.Drawable
import android.location.Location
import com.example.cyclistance.feature_mapping.domain.model.*
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): UserItem
    suspend fun getUsers(): User
    suspend fun createUser(userItem: UserItem)
    suspend fun deleteUser(id: String)


    suspend fun deleteRescueRespondent(userId: String, respondentId: String)
    suspend fun addRescueRespondent(userId: String, respondentId: String)
    suspend fun deleteAllRespondents(userId: String)


    suspend fun getRescueTransactionById(userId: String): RescueTransactionItem
    suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem)
    suspend fun deleteRescueTransaction(id: String)


    suspend fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    suspend fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

    suspend fun getUserLocation(): Flow<Location>

    suspend fun imageUrlToDrawable(imageUrl: String): Drawable

    suspend fun getUserUpdates():Flow<User>
    suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel>
    suspend fun broadcastUser()
    suspend fun broadcastRescueTransaction()
    suspend fun broadcastLocation(locationModel: LiveLocationWSModel)



    suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection
}