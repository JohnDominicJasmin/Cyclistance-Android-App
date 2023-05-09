package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

sealed class EditProfileUiEvent {
    data class UpdateUserProfileSuccess(val reason: String) : EditProfileUiEvent()
    data class GetPhotoUrlSuccess(val photoUrl: String) : EditProfileUiEvent()
    data class GetNameSuccess(val name: String) : EditProfileUiEvent()
    data class GetNameFailed(val reason: String) : EditProfileUiEvent()
    data class GetPhoneNumberSuccess(val phoneNumber: String) : EditProfileUiEvent()
    data class GetPhoneNumberFailed(val reason: String) : EditProfileUiEvent()
    object NoInternetConnection: EditProfileUiEvent()

}