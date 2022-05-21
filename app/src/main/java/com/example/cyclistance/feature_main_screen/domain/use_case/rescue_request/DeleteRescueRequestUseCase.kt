package com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request

import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class DeleteRescueRequestUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String){
        repository.deleteRescueRequest(id)
    }
}