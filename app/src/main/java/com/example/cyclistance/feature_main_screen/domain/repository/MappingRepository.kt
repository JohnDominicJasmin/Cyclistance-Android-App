package com.example.cyclistance.feature_main_screen.domain.repository

import com.example.cyclistance.feature_main_screen.data.remote.dto.RescueRequestDto
import com.example.cyclistance.feature_main_screen.domain.model.*

interface MappingRepository {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers(): List<User>
    suspend fun createUser(user: User)
    suspend fun updateUser(itemId: String, user: User)


    suspend fun getUserAssistanceById(userId: String): UserAssistance
    suspend fun getUsersAssistance(): List<UserAssistance>
    suspend fun createUserAssistance(userAssistance: UserAssistance)
    suspend fun updateUserAssistance(itemId: String, userAssistance: UserAssistance)

    suspend fun getRescueRequest(eventId: String): RescueRequest
    suspend fun createRescueRequest(rescueRequest: RescueRequest)
    suspend fun updateRescueRequest(eventId: String, rescueRequest: RescueRequest)

    suspend fun getCancellationById(userId: String, clientId: String): CancellationEvent
    suspend fun createCancellationEvent(cancellationEvent: CancellationEvent)
    suspend fun updateCancellationEvent(itemId: String, cancellationEvent: CancellationEvent)
}