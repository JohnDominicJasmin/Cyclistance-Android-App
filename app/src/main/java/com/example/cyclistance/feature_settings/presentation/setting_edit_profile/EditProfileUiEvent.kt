package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

sealed class EditProfileUiEvent {
    data class UpdateUserProfileSuccess(val reason: String) : EditProfileUiEvent()

}