package com.example.cyclistance.feature_user_profile.domain.use_case

import com.example.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UpdateUserActivityUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(id: String, userActivity: UserActivityModel){
        repository.updateUserActivity(id = id, userActivity = userActivity)
    }
}