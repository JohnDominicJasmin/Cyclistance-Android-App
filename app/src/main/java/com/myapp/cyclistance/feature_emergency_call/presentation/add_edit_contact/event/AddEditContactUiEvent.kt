package com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class AddEditContactUiEvent{
    data class OnChangeName(val name: TextFieldValue) : AddEditContactUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: TextFieldValue) : AddEditContactUiEvent()
    data object CancelEditContact : AddEditContactUiEvent()
    data object SaveEditContact : AddEditContactUiEvent()
    data object ToggleBottomSheet : AddEditContactUiEvent()
    data object SelectImageFromGallery: AddEditContactUiEvent()
    data object OpenCamera : AddEditContactUiEvent()
    data object DismissCameraDialog : AddEditContactUiEvent()
    data object DismissFilesAndMediaDialog : AddEditContactUiEvent()
}
