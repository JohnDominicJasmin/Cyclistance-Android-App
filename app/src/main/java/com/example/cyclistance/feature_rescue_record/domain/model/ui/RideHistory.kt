package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class RideHistory(
    val items: List<RideHistoryItem>,
) : Parcelable{
    @StableState
    constructor(): this(
        items = emptyList())
}
