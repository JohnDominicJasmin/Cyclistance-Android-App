package com.example.cyclistance.feature_main_screen.domain.use_case.cancellation

import com.example.cyclistance.feature_main_screen.domain.model.CancellationEvent
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class UpdateCancellationEventUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(itemId: String, cancellationEvent: CancellationEvent) {
        repository.updateCancellationEvent(itemId,cancellationEvent)
    }
}