package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state

import android.os.Parcelable
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.google.android.material.bottomsheet.BottomSheetBehavior.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueDetailsUiState(
    val rideSummary: RideSummary? = null,
):Parcelable
