package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class EditProfileUiEvent {
    object SelectImageFromGallery : EditProfileUiEvent()
    object OpenCamera : EditProfileUiEvent()
    data class OnChangeName(val name: TextFieldValue) : EditProfileUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: TextFieldValue) : EditProfileUiEvent()
    object CancelEditProfile : EditProfileUiEvent()
    object ConfirmEditProfile : EditProfileUiEvent()
    object DismissNoInternetDialog : EditProfileUiEvent()
    object ToggleBottomSheet : EditProfileUiEvent()
    object DismissFilesAndMediaDialog : EditProfileUiEvent()
    object DismissCameraDialog : EditProfileUiEvent()


}
