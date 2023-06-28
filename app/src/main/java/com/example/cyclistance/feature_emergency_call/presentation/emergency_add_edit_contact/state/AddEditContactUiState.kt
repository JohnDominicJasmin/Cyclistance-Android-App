package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class AddEditContactUiState(
    val selectedImageUri: String = "",
    val photoUrl: String = "",
    val name: @RawValue TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val phoneNumber: @RawValue TextFieldValue = TextFieldValue(""),
    val phoneNumberErrorMessage: String = "",
    val cameraPermissionDialogVisible: Boolean = false,
    val filesAndMediaDialogVisible: Boolean = false,
    val idCurrentlyEditing: String? = null,
) : Parcelable
