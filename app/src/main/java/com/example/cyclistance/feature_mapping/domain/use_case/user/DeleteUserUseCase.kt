package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class DeleteUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(id: String){
        repository.deleteUser(id)
    }
}