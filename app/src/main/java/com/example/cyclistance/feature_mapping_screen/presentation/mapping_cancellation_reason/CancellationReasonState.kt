package com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation_reason

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class CancellationReasonState(
    val selectedReason: String = "",
    val hasInternet: Boolean = true,
    val isLoading: Boolean = false,
    val reasonErrorMessage: String = "",
    val message: String = "",
):Parcelable