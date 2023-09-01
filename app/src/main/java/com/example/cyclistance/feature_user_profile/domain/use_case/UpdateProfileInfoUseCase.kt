package com.example.cyclistance.feature_user_profile.domain.use_case

import com.example.cyclistance.feature_user_profile.domain.exceptions.UserProfileExceptions
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UpdateProfileInfoUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(id: String, userProfile: UserProfileInfoModel) {

        if (userProfile.address.isEmpty()) {
            throw UserProfileExceptions.AddressException()
        }

        repository.updateUserProfileInfo(id = id, userProfile = userProfile)
    }
}