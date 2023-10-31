package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state

import android.os.Parcelable
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.google.android.material.bottomsheet.BottomSheetBehavior.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueDetailsUiState(
    val rescueRide: RescueRide? = null
):Parcelable
