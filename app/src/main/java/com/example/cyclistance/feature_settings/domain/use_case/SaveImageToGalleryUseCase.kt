package com.example.cyclistance.feature_settings.domain.use_case

import android.graphics.Bitmap
import android.net.Uri
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class SaveImageToGalleryUseCase(private val settingRepository: SettingRepository) {

    suspend operator fun invoke(bitmap: Bitmap): Uri?{
        return settingRepository.saveImageToGallery(bitmap)
    }
}