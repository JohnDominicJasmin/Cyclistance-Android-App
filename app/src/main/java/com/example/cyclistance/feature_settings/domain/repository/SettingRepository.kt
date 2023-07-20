package com.example.cyclistance.feature_settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    suspend fun toggleTheme(value: Boolean)
    fun isDarkTheme(): Flow<Boolean>
    suspend fun updatePhoneNumber(phoneNumber: String)
    fun getPhoneNumber(): Flow<String>
    suspend fun updateProfile(photoUri: String?, name: String?): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun getName(): String?
    suspend fun getPhotoUrl(): String?
}