package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state

import android.os.Parcelable
import com.google.android.material.bottomsheet.BottomSheetBehavior.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class CancellationReasonUiState(
    val isNoInternetVisible: Boolean = false,
    val selectedReason: String = "",
    val selectedReasonErrorMessage: String = "",
):Parcelable
