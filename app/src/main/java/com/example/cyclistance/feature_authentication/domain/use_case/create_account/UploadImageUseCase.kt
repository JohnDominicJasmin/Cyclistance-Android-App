package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class UploadImageUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {
    suspend operator fun invoke(uri: Uri): Uri{
       return repository.uploadImage(uri)
    }
}