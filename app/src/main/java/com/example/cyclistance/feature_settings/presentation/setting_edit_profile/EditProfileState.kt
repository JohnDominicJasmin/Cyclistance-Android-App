package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileState(
    val name: String = "",
    var phoneNumber: String = "",
    val photoUrl: String = "",

    val nameSnapshot: String = "",
    val phoneNumberSnapshot: String = "",

    val nameErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",
    val isLoading: Boolean = false,
    val galleryButtonClick: Boolean = false,
    val imageUri: String? = ""
    ):Parcelable