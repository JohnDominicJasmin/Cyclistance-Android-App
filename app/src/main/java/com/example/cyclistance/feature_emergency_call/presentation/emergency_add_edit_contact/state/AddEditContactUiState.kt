package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class AddEditContactUiState(
    val selectedImageUri: String = "",
    val photoUrl: String = "",
    val name: String = "",
    val nameErrorMessage: String = "",
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String = "",
    val cameraPermissionDialogVisible: Boolean = false,
    val filesAndMediaDialogVisible: Boolean = false,
    val idCurrentlyEditing: String? = null,
) : Parcelable
