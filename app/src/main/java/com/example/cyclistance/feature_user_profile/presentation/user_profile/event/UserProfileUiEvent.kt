package com.example.cyclistance.feature_user_profile.presentation.user_profile.event

sealed class UserProfileUiEvent{
    object OnClickEditProfile: UserProfileUiEvent()
    data class OnClickMessageProfile(val userId: String): UserProfileUiEvent()

}