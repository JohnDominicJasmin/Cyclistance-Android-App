package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Immutable
@Parcelize
data class CancelledRescueModel(
    val transactionID:String? = null,
    val rescueCancelledBy:String? = null,
    val reason:String? = null,
    val message:String? = null
):Parcelable
