package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class GetPhotoUrlUseCase(private val repository: AuthRepository<*>) {
    operator fun invoke(): String {
        return repository.getPhotoUrl().takeIf { it != "null" } ?: IMAGE_PLACEHOLDER_URL
    }
}