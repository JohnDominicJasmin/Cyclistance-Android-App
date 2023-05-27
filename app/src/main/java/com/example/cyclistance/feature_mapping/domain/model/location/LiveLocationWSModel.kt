package com.example.cyclistance.feature_mapping.domain.model.location

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class LiveLocationWSModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val room: String? = null,
):Parcelable
