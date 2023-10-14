package com.example.cyclistance.feature_user_profile.presentation.user_profile.event

sealed class UserProfileUiEvent{
    data object OnClickEditProfile: UserProfileUiEvent()
    data object OnClickRideHistory: UserProfileUiEvent()

}
