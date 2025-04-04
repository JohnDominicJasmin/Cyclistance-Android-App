package com.myapp.cyclistance.feature_user_profile.domain.use_case

import com.myapp.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UploadImageUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(uri: String): String{
       return repository.uploadImage(uri)
    }
}