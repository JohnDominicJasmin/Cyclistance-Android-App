package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class EditProfileUiState(
    val selectedImageUri: String = "",
    val photoUrl: String = "",
    val name: String = "",
    val nameErrorMessage: String = "",
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String = "",
    val noInternetVisible: Boolean = false
):Parcelable
