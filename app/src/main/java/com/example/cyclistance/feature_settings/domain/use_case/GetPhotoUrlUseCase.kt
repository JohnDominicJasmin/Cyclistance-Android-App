package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class GetPhotoUrlUseCase(private val repository: SettingRepository) {
    suspend operator fun invoke(): String {
        return repository.getPhotoUrl().takeIf { it != "null" } ?: IMAGE_PLACEHOLDER_URL
    }
}