package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.state

import android.os.Parcelable
import com.google.android.material.bottomsheet.BottomSheetBehavior.StableState
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueDetailsUiState(
    val rideSummary: RideSummary? = null,
    val rideMetrics: RideMetrics? = null
):Parcelable
