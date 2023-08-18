package com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class HazardousLaneMarker(
    val id: String = "",
    val idCreator: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val label: String = ""
):Parcelable
