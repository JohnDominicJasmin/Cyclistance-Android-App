package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class HazardousLane(
    val markers: List<HazardousLaneMarkerDetails>
):Parcelable{
    @StableState
    constructor(): this(
        markers = emptyList()
    )
}
