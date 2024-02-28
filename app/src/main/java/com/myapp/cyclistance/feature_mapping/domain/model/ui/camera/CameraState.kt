package com.myapp.cyclistance.feature_mapping.domain.model.ui.camera

import android.os.Parcelable
import com.mapbox.mapboxsdk.geometry.LatLng
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class CameraState(
    val position: LatLng = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE),
    val zoom: Double = DEFAULT_MAP_ZOOM_LEVEL,
):Parcelable
