package com.example.cyclistance.feature_user_profile.presentation.user_profile.event

import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel

sealed class UserProfileEvent{
    data class GetUserProfileSuccess(val profile: UserProfileModel): UserProfileEvent()
}
