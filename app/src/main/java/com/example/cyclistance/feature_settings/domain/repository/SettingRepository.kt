package com.example.cyclistance.feature_settings.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun isDarkTheme(): Flow<Boolean>
    suspend fun toggleTheme()
    suspend fun saveImageToGallery(bitmap: Bitmap):Uri?
}