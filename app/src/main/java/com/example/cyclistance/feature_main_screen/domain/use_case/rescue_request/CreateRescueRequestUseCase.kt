package com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request

import com.example.cyclistance.feature_main_screen.domain.model.RescueRequest
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class CreateRescueRequestUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(rescueRequest: RescueRequest){
        repository.createRescueRequest(rescueRequest)
    }
}