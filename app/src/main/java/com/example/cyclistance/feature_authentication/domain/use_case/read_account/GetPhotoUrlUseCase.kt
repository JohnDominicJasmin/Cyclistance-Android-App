package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetPhotoUrlUseCase(private val repository: AuthRepository<AuthCredential>) {
    operator fun invoke(): String {
        return repository.getPhotoUrl().takeIf { it != "null" } ?: IMAGE_PLACEHOLDER_URL
    }
}