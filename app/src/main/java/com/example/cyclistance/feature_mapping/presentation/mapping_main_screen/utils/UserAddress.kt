package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

import android.location.Address
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize


@Parcelize
@Immutable
@Stable
data class UserAddress(
    val address: Address? = null
) : Parcelable







