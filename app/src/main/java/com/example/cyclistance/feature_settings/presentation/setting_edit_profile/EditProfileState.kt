package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.compose.ui.text.input.TextFieldValue

data class EditProfileState(
    val name: TextFieldValue = TextFieldValue(""),
    var phoneNumber: TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",
    val photoUrl: String = "",
    val isLoading: Boolean = false,

    )