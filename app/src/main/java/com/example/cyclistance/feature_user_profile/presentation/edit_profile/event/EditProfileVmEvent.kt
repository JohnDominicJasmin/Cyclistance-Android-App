package com.example.cyclistance.feature_user_profile.presentation.edit_profile.event

import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel

sealed class EditProfileVmEvent {

    data class Save(val userProfile: UserProfileInfoModel): EditProfileVmEvent()
    object LoadProfile: EditProfileVmEvent()
    object LoadProfileInfo: EditProfileVmEvent()





}