package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class EditProfileUiEvent {
    data object SelectImageFromGallery : EditProfileUiEvent()
    data object OpenCamera : EditProfileUiEvent()
    data class OnChangeName(val name: TextFieldValue) : EditProfileUiEvent()
    data class OnChangeCyclingGroup(val cyclingGroup: TextFieldValue) : EditProfileUiEvent()
    data class OnChangeAddress(val address: TextFieldValue) : EditProfileUiEvent()
    data object CancelEditProfile : EditProfileUiEvent()
    data object ConfirmEditProfile : EditProfileUiEvent()
    data object DismissNoInternetDialog : EditProfileUiEvent()
    data object ToggleBottomSheet : EditProfileUiEvent()
    data object DismissFilesAndMediaDialog : EditProfileUiEvent()
    data object DismissCameraDialog : EditProfileUiEvent()
    data object AllowProminentCameraDialog : EditProfileUiEvent()
    data object AllowProminentGalleryDialog : EditProfileUiEvent()
    data object DismissProminentCameraDialog : EditProfileUiEvent()
    data object DismissProminentGalleryDialog : EditProfileUiEvent()


}
