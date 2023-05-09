package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

sealed class EditProfileEvent {

    data class Save(val imageUri: String, val phoneNumber: String, val name: String): EditProfileEvent()





}