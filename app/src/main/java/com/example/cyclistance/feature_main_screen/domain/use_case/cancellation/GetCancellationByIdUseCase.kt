package com.example.cyclistance.feature_main_screen.domain.use_case.cancellation

import com.example.cyclistance.feature_main_screen.domain.model.CancellationEvent
import com.example.cyclistance.feature_main_screen.domain.model.HelpRequest
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class GetCancellationByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String, clientId: String): CancellationEvent {
        return repository.getCancellationById(userId, clientId)
    }
}