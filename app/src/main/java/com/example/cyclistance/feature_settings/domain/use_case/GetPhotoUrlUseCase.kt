package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class GetPhotoUrlUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(): String {
        return repository.getPhotoUrl().takeIf { it != "null" } ?: IMAGE_PLACEHOLDER_URL
    }
}