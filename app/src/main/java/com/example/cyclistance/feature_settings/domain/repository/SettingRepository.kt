package com.example.cyclistance.feature_settings.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    suspend fun saveImageToGallery(bitmap: Bitmap):Uri?
    suspend fun toggleTheme(value: Boolean)
    fun isDarkTheme(): Flow<Boolean?>
}