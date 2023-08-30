package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event

sealed class EditProfileVmEvent {

    data class Save(val imageUri: String, val name: String): EditProfileVmEvent()
    object LoadProfile: EditProfileVmEvent()




}