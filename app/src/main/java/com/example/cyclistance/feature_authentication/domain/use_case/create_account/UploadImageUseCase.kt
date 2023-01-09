package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class UploadImageUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uri: String): String{
       return repository.uploadImage(uri)
    }
}