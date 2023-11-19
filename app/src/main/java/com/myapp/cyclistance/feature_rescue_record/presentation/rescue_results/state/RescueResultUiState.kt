package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueResultUiState(
    val step: Int = 1,
    val rating: Float = 0.0f
):Parcelable
