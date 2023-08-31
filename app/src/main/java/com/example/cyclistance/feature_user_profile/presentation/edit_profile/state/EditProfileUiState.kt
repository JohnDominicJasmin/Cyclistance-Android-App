package com.example.cyclistance.feature_user_profile.presentation.edit_profile.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class EditProfileUiState(
    val selectedImageUri: String = "",
    val photoUrl: String = "",
    val nameErrorMessage: String = "",
    val cyclingGroupErrorMessage: String = "",
    val addressErrorMessage: String = "",
    val noInternetVisible: Boolean = false,
    val cameraPermissionDialogVisible: Boolean = false,
    val filesAndMediaDialogVisible: Boolean = false,
):Parcelable
