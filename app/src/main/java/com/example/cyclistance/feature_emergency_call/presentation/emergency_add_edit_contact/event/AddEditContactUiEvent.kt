package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event

sealed class AddEditContactUiEvent {
    object SelectImageFromGallery : AddEditContactUiEvent()
    object OpenCamera : AddEditContactUiEvent()
    data class OnChangeName(val name: String) : AddEditContactUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: String) : AddEditContactUiEvent()
    object CancelEditContact : AddEditContactUiEvent()
    object SaveEditContact : AddEditContactUiEvent()
    object ToggleBottomSheet : AddEditContactUiEvent()
    object DismissFilesAndMediaDialog : AddEditContactUiEvent()
    object DismissCameraDialog : AddEditContactUiEvent()
    object CloseEditContactScreen : AddEditContactUiEvent()
}
