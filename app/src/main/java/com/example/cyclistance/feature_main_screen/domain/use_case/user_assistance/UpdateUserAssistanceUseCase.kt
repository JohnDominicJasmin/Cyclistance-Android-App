package com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance

import com.example.cyclistance.feature_main_screen.domain.model.UserAssistance
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class UpdateUserAssistanceUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(itemId:String, userAssistance: UserAssistance){
        repository.updateUserAssistance(itemId, userAssistance)
    }
}