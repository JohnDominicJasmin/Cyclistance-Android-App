package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class ImageBitmap(val bitmap: Bitmap? = null)

data class EditProfileState(
    val name: String = "",
    var phoneNumber: String = "",
    val nameErrorMessage: String = "",
    val phoneNumberErrorMessage: String = "",
    val photoUrl: String = "",
    val isLoading: Boolean = false,
    val imageBitmap: ImageBitmap = ImageBitmap()
    )