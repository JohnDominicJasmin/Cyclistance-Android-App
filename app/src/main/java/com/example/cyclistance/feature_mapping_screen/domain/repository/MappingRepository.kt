package com.example.cyclistance.feature_mapping_screen.domain.repository

import android.graphics.drawable.Drawable
import android.location.Location
import com.example.cyclistance.feature_mapping_screen.domain.model.*
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


    fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

    fun getUserLocation(): Flow<Location>

    suspend fun imageUrlToDrawable(imageUrl: String): Drawable

    fun getUserUpdates():Flow<User>
    fun getRescueTransactionUpdates(): Flow<RescueTransaction>
    fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel>
    fun broadcastUser()
    fun broadcastRescueTransaction()
    fun broadcastLocation(locationModel: LiveLocationWSModel)


}