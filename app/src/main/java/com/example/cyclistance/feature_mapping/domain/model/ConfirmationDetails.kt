package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ConfirmationDetails(
    val address: String,
    val bikeType: String,
    val description: String,
    val message: String,
) : Parcelable
