package com.example.cyclistance.feature_rescue_record.presentation.history_details.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class HistoryDetailsState(
    val isLoading: Boolean = false,
):Parcelable
