package com.example.cyclistance.feature_mapping_screen.domain.repository

import android.graphics.drawable.Drawable
import android.location.Location
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): UserItem
    suspend fun getUsers(): User
    suspend fun createUser(userItem: UserItem)
    suspend fun updateUser(itemId: String, userItem: UserItem)
    suspend fun deleteUser(id: String)

    suspend fun getRescueTransactionById(userId: String): RescueTransaction
    suspend fun createRescueTransaction(rescueTransaction: RescueTransaction)
    suspend fun updateRescueTransaction(itemId: String, rescueTransaction: RescueTransaction)
    suspend fun deleteRescueTransaction(id: String)

    fun getBikeType(): Flow<String>
    suspend fun updateBikeType(bikeType: String)

    fun getAddress(): Flow<String>
    suspend fun updateAddress(address: String)

    fun getUserLocation(): Flow<Location>

    suspend fun imageUrlToDrawable(imageUrl: String): Drawable


}