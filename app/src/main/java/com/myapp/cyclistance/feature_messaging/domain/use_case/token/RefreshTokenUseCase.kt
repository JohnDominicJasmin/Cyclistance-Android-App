package com.myapp.cyclistance.feature_messaging.domain.use_case.token

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class RefreshTokenUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke() {
        repository.refreshToken()
    }
}