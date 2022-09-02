package com.example.cyclistance.feature_main_screen.domain.repository

import com.example.cyclistance.feature_main_screen.domain.model.*
import com.example.cyclistance.core.utils.SharedLocationModel
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User)
    suspend fun updateUser(itemId: String, user: User)
    suspend fun deleteUser(id: String)

    fun getBikeType(): Flow<String?>
    suspend fun updateBikeType(bikeType: String)



    fun getUserLocation(): Flow<SharedLocationModel>

}