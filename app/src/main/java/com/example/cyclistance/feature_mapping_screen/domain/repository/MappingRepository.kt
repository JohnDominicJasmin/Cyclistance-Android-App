package com.example.cyclistance.feature_mapping_screen.domain.repository

import com.example.cyclistance.core.utils.SharedLocationModel
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User)
    suspend fun updateUser(itemId: String, user: User)
    suspend fun deleteUser(id: String)

    fun getBikeType(): Flow<String>
    suspend fun updateBikeType(bikeType: String)

    fun getAddress(): Flow<String>
    suspend fun updateAddress(address: String)

    fun getUserLocation(): Flow<SharedLocationModel>


}