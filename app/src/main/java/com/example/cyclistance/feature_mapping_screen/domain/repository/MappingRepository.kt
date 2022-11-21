package com.example.cyclistance.feature_mapping_screen.domain.repository

import android.graphics.drawable.Drawable
import android.location.Location
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): UserItem
    suspend fun getUsers(): User
    suspend fun createUser(userItem: UserItem)
    suspend fun deleteUser(id: String)

    suspend fun getRescueTransactionById(userId: String): RescueTransactionItem
    suspend fun getRescueTransactions(): RescueTransaction
    suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem)
    suspend fun deleteRescueTransaction(id: String)


    fun getBikeType(): Flow<String>
    suspend fun updateBikeType(bikeType: String)

    fun getAddress(): Flow<String>
    suspend fun updateAddress(address: String)

    fun getUserLocation(): Flow<Location>

    suspend fun imageUrlToDrawable(imageUrl: String): Drawable

    fun getUserUpdates():Flow<User>
    fun broadCastUser()
    fun broadCastRescueTransaction()
    fun getRescueTransactionUpdates(): Flow<RescueTransaction>
}