package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.compose.ui.text.input.TextFieldValue

data class EditProfileState(
    val name: TextFieldValue = TextFieldValue(""),
    val address: TextFieldValue = TextFieldValue(""),
    val phoneNumber: TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val addressErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",
    val isLoading: Boolean = false,

)