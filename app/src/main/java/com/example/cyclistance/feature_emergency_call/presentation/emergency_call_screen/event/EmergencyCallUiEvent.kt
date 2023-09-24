package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class EmergencyCallUiEvent {
    data object OnClickAddContact : EmergencyCallUiEvent()
    data class OnClickContact(val phoneNumber: String) : EmergencyCallUiEvent()
    data class OnClickEditContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class OnClickDeleteContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class DeleteContact(val emergencyContact: EmergencyContactModel) : EmergencyCallUiEvent()
    data object DismissDeleteContactDialog : EmergencyCallUiEvent()
    data object DismissMaximumContactDialog : EmergencyCallUiEvent()

    data class OnChangeName(val name: TextFieldValue) : EmergencyCallUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: TextFieldValue) : EmergencyCallUiEvent()
    data object CancelEditContact : EmergencyCallUiEvent()
    data object SaveEditContact : EmergencyCallUiEvent()
    data object ToggleBottomSheet : EmergencyCallUiEvent()
    data object DismissEditContactScreen : EmergencyCallUiEvent()

    data object SelectImageFromGallery: EmergencyCallUiEvent()
    data object OpenCamera : EmergencyCallUiEvent()
    data object DismissCameraDialog : EmergencyCallUiEvent()
    data object DismissFilesAndMediaDialog : EmergencyCallUiEvent()


}
