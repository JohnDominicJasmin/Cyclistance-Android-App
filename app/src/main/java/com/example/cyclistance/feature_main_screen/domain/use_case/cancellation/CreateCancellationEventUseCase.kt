package com.example.cyclistance.feature_main_screen.domain.use_case.cancellation

import com.example.cyclistance.feature_main_screen.domain.model.CancellationEvent
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class CreateCancellationEventUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(cancellationEvent: CancellationEvent) {
        repository.createCancellationEvent(cancellationEvent)
    }
}