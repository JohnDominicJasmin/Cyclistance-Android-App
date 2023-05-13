package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event

sealed class EditProfileEvent {
    data class UpdateUserProfileSuccess(val reason: String) : EditProfileEvent()
    data class GetPhotoUrlSuccess(val photoUrl: String) : EditProfileEvent()
    data class GetNameSuccess(val name: String) : EditProfileEvent()
    data class GetNameFailed(val reason: String) : EditProfileEvent()
    data class GetPhoneNumberSuccess(val phoneNumber: String) : EditProfileEvent()
    data class GetPhoneNumberFailed(val reason: String) : EditProfileEvent()
    object NoInternetConnection: EditProfileEvent()

}