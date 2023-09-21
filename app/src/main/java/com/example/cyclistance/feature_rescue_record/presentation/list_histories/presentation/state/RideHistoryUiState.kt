package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideHistoryUiState(
    val rideHistory: RideHistory = RideHistory(),
):Parcelable
