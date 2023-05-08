package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class CancellationReasonState(
    val isLoading: Boolean = false,
):Parcelable