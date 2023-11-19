package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date


@StableState
@Parcelize
data class HazardousLaneMarker(
    val id: String,
    val idCreator: String,
    val latitude: Double?,
    val longitude: Double?,
    val label: String,
    val datePosted: Date,
    val description: String,
    val address: String,
) : Parcelable {
    @StableState
    constructor() : this(
        id = "",
        idCreator = "",
        latitude = 0.0,
        longitude = 0.0,
        label = "",
        datePosted = Date(),
        description = "",
        address = "",
    )
}
