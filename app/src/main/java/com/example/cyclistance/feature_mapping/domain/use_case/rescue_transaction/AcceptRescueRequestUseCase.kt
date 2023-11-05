package com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class AcceptRescueRequestUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(userId: String, rescuerId: String) {
        repository.acceptRescueRequest(userId = userId, rescuerId = rescuerId)
    }
}