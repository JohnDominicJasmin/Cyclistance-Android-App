package com.example.cyclistance.feature_main_screen.domain.use_case.help_request

import com.example.cyclistance.feature_main_screen.domain.model.HelpRequest
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class GetHelpRequestByIdUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(userId: String, clientId: String): HelpRequest{
        return repository.getHelpRequestById(userId, clientId)
    }
}