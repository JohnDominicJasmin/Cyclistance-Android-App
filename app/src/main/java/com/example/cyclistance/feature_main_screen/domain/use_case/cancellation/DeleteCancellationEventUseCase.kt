package com.example.cyclistance.feature_main_screen.domain.use_case.cancellation

import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class DeleteCancellationEventUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String){
        repository.deleteCancellationEvent(id)
    }
}