package com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance

import com.example.cyclistance.feature_main_screen.domain.model.UserAssistance
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserAssistanceByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String): UserAssistance  {
        return repository.getUserAssistanceById(id)
    }
}