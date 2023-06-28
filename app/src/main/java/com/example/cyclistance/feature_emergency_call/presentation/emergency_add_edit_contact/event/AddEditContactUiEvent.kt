package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class AddEditContactUiEvent {
    object SelectImageFromGallery : AddEditContactUiEvent()
    object OpenCamera : AddEditContactUiEvent()
    data class OnChangeName(val name: TextFieldValue) : AddEditContactUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: TextFieldValue) : AddEditContactUiEvent()
    object CancelEditContact : AddEditContactUiEvent()
    object SaveEditContact : AddEditContactUiEvent()
    object ToggleBottomSheet : AddEditContactUiEvent()
    object DismissFilesAndMediaDialog : AddEditContactUiEvent()
    object DismissCameraDialog : AddEditContactUiEvent()
    object CloseEditContactScreen : AddEditContactUiEvent()
}
