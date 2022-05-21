package com.example.cyclistance.feature_main_screen.domain.use_case.user

import com.example.cyclistance.feature_main_screen.domain.model.User
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase

class UpdateUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(itemId: String, user: User){
        repository.updateUser(itemId, user)
    }
}