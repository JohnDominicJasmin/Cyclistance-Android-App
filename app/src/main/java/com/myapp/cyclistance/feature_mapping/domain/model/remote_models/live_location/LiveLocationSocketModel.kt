package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class LiveLocationSocketModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val room: String? = null,
):Parcelable
