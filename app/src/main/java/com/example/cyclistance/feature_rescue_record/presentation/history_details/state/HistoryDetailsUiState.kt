package com.example.cyclistance.feature_rescue_record.presentation.history_details.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class HistoryDetailsUiState(
    val rescueRide: RescueRide? = null
): Parcelable
