package com.myapp.cyclistance.feature_messaging.domain.use_case.network

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class ReEnableNetworkSyncUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke() {
        repository.reEnableNetworkSync()
    }
}