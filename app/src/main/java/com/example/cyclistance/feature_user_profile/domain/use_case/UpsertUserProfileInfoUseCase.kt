package com.example.cyclistance.feature_user_profile.domain.use_case

import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UpsertUserProfileInfoUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(userProfile: UserProfileInfoModel) {
        repository.upsertUserProfileInfo(userProfile)
    }
}