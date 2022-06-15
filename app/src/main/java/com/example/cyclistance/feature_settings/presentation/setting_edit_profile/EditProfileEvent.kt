package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.compose.ui.text.input.TextFieldValue

sealed class EditProfileEvent {

    object SaveProfile: EditProfileEvent()
    data class EnteredPhoneNumber(val phoneNumber: TextFieldValue): EditProfileEvent()
    data class EnteredName(val name: TextFieldValue): EditProfileEvent()
    object LoadPhoto: EditProfileEvent()
    object LoadName: EditProfileEvent()
    object LoadPhoneNumber: EditProfileEvent()

}