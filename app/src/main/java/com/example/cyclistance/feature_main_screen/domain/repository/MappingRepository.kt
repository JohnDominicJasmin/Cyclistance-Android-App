package com.example.cyclistance.feature_main_screen.domain.repository

import com.example.cyclistance.feature_main_screen.domain.model.*
import com.example.cyclistance.utils.SavePreferences
import com.example.cyclistance.utils.SharedLocationModel
import kotlinx.coroutines.flow.Flow

interface MappingRepository:SavePreferences<String> {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User)
    suspend fun updateUser(itemId: String, user: User)
    suspend fun deleteUser(id: String)


    suspend fun getRescueRequest(eventId: String): RescueRequest
    suspend fun createRescueRequest(rescueRequest: RescueRequest)
    suspend fun updateRescueRequest(eventId: String, rescueRequest: RescueRequest)
    suspend fun deleteRescueRequest(id: String)


    suspend fun getCancellationById(userId: String, clientId: String): CancellationEvent
    suspend fun createCancellationEvent(cancellationEvent: CancellationEvent)
    suspend fun updateCancellationEvent(itemId: String, cancellationEvent: CancellationEvent)
    suspend fun deleteCancellationEvent(id: String)


    fun getUserLocation(): Flow<SharedLocationModel>

}