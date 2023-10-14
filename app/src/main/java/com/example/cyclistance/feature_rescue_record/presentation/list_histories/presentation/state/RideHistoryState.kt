package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideHistoryState(
    val isLoading: Boolean = false,
    val uid: String = ""
):Parcelable
