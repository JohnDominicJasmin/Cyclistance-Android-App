package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class UploadImageUseCase(private val repository: SettingRepository) {
    suspend operator fun invoke(uri: String): String{
       return repository.uploadImage(uri)
    }
}