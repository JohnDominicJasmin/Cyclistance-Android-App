package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

sealed class EditProfileEvent {

    object Save: EditProfileEvent()
    data class EnterPhoneNumber(val phoneNumber: String): EditProfileEvent()
    data class EnterName(val name: String): EditProfileEvent()

    object LoadPhoto: EditProfileEvent()
    object LoadName: EditProfileEvent()
    object LoadPhoneNumber: EditProfileEvent()

    data class SelectImageUri(val uri: String?): EditProfileEvent()
    object OnClickGalleryButton : EditProfileEvent()
    object DismissNoInternetDialog: EditProfileEvent()

}