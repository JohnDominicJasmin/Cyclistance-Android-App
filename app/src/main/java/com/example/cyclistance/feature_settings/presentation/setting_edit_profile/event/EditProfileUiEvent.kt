package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event

sealed class EditProfileUiEvent{
    object SelectImageFromGallery : EditProfileUiEvent()
    object OpenCamera : EditProfileUiEvent()
    data class ChangeName(val name: String) : EditProfileUiEvent()
    data class ChangePhoneNumber(val phoneNumber: String) : EditProfileUiEvent()
    object CancelEditProfile : EditProfileUiEvent()
    object ConfirmEditProfile : EditProfileUiEvent()
    object DismissNoInternetDialog : EditProfileUiEvent()
    object ToggleBottomSheet : EditProfileUiEvent()
    object DismissFilesAndMediaDialog : EditProfileUiEvent()
    object DismissCameraDialog : EditProfileUiEvent()


}
