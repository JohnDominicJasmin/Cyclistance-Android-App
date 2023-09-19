package com.example.cyclistance.feature_rescue_outcome.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class RideHistory(
    val histories: List<RideHistoryItemModel> = listOf(),
) : Parcelable
