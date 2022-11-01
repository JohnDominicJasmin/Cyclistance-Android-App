package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize


@Parcelize
@Immutable
@Stable
data class NearbyCyclists(
    val activeUsers: List<Cyclist> = emptyList(),
) : Parcelable
