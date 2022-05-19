package com.example.cyclistance.feature_main_screen.domain.repository

import com.example.cyclistance.feature_main_screen.domain.model.*

interface MappingRepository {
    suspend fun getUserById(userId: String): User
    suspend fun getUsers():List<User>

    suspend fun getUserAssistanceById(userId: String): UserAssistance
    suspend fun getUsersAssistance():List<UserAssistance>

    suspend fun getHelpRequestById(userId: String, clientId: String):HelpRequest
    suspend fun getCancellationById(userId: String, clientId: String):CancellationEvent
}