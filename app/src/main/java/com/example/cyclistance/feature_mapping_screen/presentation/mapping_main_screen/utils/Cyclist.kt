package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class Cyclist(
    val user: User = User(),
    val profileImageBitmap: Bitmap? = null,
) : Parcelable
