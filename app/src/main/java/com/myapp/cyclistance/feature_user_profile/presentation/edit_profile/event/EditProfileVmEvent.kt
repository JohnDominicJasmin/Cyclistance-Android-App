package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event

import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel

sealed class EditProfileVmEvent {

    data class Save(val userProfile: UserProfileInfoModel): EditProfileVmEvent()
    data object LoadProfile: EditProfileVmEvent()
    data object LoadUserProfileInfo: EditProfileVmEvent()





}