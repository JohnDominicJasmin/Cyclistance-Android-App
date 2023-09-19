package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueResultState(
    val isLoading: Boolean = false,
    val rescuerName: String = "",
    val rescuerId: String = "",
    val rescuerPhoto: String = "",
):Parcelable
