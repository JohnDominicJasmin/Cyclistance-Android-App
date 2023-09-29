package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class AddEditContactUiState(
    val selectedImageUri: String? = null,
    val nameErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",

    val cameraPermissionDialogVisible: Boolean = false,
    val filesAndMediaDialogVisible: Boolean = false,
) : Parcelable

