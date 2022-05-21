package com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance

import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class DeleteUserAssistanceUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String){
        repository.deleteUserAssistance(id)
    }
}