package com.myapp.cyclistance.feature_settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    suspend fun toggleTheme(value: Boolean)
    fun isDarkTheme(): Flow<Boolean>

}