package com.example.cyclistance.feature_main_screen.domain.use_case.user

import com.example.cyclistance.feature_main_screen.domain.model.User
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class CreateUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(user: User){
        repository.createUser(user)
    }
}