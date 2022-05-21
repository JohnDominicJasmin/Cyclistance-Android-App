package com.example.cyclistance.feature_main_screen.domain.use_case.user

import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class DeleteUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String){
        repository.deleteUser(id)
    }
}