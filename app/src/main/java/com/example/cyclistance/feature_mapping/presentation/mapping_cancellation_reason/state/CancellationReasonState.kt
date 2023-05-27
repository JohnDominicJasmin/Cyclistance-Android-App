package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class CancellationReasonState(
    val isLoading: Boolean = false,
):Parcelable