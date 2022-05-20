package com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request

import com.example.cyclistance.feature_main_screen.domain.model.RescueRequest
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class GetRescueRequestUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String): RescueRequest{
        return repository.getRescueRequest(userId)
    }
}