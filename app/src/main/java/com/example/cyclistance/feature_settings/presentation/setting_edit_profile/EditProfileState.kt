package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class ImageBitmap(val bitmap: Bitmap? = null): Parcelable

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
    val imageBitmap: ImageBitmap = ImageBitmap()
    ):Parcelable