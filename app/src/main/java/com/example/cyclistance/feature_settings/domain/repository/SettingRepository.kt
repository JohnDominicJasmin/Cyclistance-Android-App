package com.example.cyclistance.feature_settings.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.cyclistance.utils.SavePreferences
import kotlinx.coroutines.flow.Flow

interface SettingRepository:SavePreferences<Boolean> {

    suspend fun saveImageToGallery(bitmap: Bitmap):Uri?
}