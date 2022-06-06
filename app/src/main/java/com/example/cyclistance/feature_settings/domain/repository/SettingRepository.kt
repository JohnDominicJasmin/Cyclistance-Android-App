package com.example.cyclistance.feature_settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun isDarkTheme(): Flow<Boolean>
    suspend fun toggleTheme()
}