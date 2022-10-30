package com.example.cyclistance.feature_mapping_screen.domain.repository

import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Location
import androidx.annotation.DrawableRes
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User)
    suspend fun updateUser(itemId: String, user: User): User
    suspend fun deleteUser(id: String)

    suspend fun getRescueTransactionById(userId: String): RescueTransaction
    suspend fun createRescueTransaction(rescueTransaction: RescueTransaction)
    suspend fun updateRescueTransaction(itemId: String, rescueTransaction: RescueTransaction): RescueTransaction
    suspend fun deleteRescueTransaction(id: String)

    fun getBikeType(): Flow<String>
    suspend fun updateBikeType(bikeType: String)

    fun getAddress(): Flow<String>
    suspend fun updateAddress(address: String)

    fun getUserLocation(): Flow<Location>

    suspend fun imageUrlToDrawable(imageUrl: String): Drawable


}