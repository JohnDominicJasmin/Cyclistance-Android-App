package com.example.cyclistance.feature_messaging.domain.use_case.network

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class ReEnableNetworkSyncUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke() {
        repository.reEnableNetworkSync()
    }
}