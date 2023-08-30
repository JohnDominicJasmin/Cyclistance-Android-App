package com.example.cyclistance.feature_user_profile.presentation.edit_profile.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class EditProfileUiEvent {
    object SelectImageFromGallery : EditProfileUiEvent()
    object OpenCamera : EditProfileUiEvent()
    data class OnChangeName(val name: TextFieldValue) : EditProfileUiEvent()
    data class OnChangeCyclingGroup(val cyclingGroup: TextFieldValue) : EditProfileUiEvent()
    data class OnChangeCity(val city: TextFieldValue) : EditProfileUiEvent()
    data class OnChangeProvince(val province: TextFieldValue) : EditProfileUiEvent()
    object CancelEditProfile : EditProfileUiEvent()
    object ConfirmEditProfile : EditProfileUiEvent()
    object DismissNoInternetDialog : EditProfileUiEvent()
    object ToggleBottomSheet : EditProfileUiEvent()
    object DismissFilesAndMediaDialog : EditProfileUiEvent()
    object DismissCameraDialog : EditProfileUiEvent()


}
