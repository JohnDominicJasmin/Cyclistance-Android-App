package com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event

sealed class UserProfileVmEvent{

    data class LoadProfile(val userId: String): UserProfileVmEvent()

}
