package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue

data class EditProfileState(
    val name: TextFieldValue = TextFieldValue(""),
    var phoneNumber: TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",
    val photoUrl: String = "",
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val bitmap: Bitmap? = null,
    )