package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class EditProfileUiState(
    val selectedImageUri: String = "",
    val photoUrl: String = "",
    val name: @RawValue TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val phoneNumber: @RawValue TextFieldValue = TextFieldValue(""),
    val phoneNumberErrorMessage: String = "",
    val noInternetVisible: Boolean = false,
    val cameraPermissionDialogVisible: Boolean = false,
    val filesAndMediaDialogVisible: Boolean = false,
):Parcelable
