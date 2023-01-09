package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class UploadImageUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(uri: String): String{
       return repository.uploadImage(uri)
    }
}