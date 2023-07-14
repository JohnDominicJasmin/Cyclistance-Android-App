package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class EmergencyCallUiEvent {
    object OnClickCancel : EmergencyCallUiEvent()
    object OnClickAddContact : EmergencyCallUiEvent()
    data class OnClickContact(val phoneNumber: String) : EmergencyCallUiEvent()
    data class OnClickEditContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class OnClickDeleteContact(val emergencyContact: EmergencyContactModel) :
        EmergencyCallUiEvent()

    data class DeleteContact(val emergencyContact: EmergencyContactModel) : EmergencyCallUiEvent()
    object DismissDeleteContactDialog : EmergencyCallUiEvent()
    object DismissMaximumContactDialog : EmergencyCallUiEvent()

    object SelectImageFromGallery : EmergencyCallUiEvent()
    object OpenCamera : EmergencyCallUiEvent()
    data class OnChangeName(val name: TextFieldValue) : EmergencyCallUiEvent()
    data class OnChangePhoneNumber(val phoneNumber: TextFieldValue) : EmergencyCallUiEvent()
    object CancelEditContact : EmergencyCallUiEvent()
    object SaveEditContact : EmergencyCallUiEvent()
    object ToggleBottomSheet : EmergencyCallUiEvent()
    object DismissFilesAndMediaDialog : EmergencyCallUiEvent()
    object DismissCameraDialog : EmergencyCallUiEvent()
    object DismissEditContactScreen : EmergencyCallUiEvent()


}
