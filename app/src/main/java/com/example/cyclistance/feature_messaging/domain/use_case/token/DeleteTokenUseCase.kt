package com.example.cyclistance.feature_messaging.domain.use_case.token

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class DeleteTokenUseCase(private val repository: MessagingRepository) {

    operator fun invoke() {
        repository.deleteToken()
    }
}