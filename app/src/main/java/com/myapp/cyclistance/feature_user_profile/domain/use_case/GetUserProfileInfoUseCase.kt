package com.myapp.cyclistance.feature_user_profile.domain.use_case

import com.myapp.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class GetUserProfileInfoUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(id: String) = repository.getUserProfile(id)
}