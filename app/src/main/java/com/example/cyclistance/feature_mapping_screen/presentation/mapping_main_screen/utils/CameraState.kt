package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize

@Stable
@Immutable
@Parcelize
data class CameraState(
    val cameraPosition: Point = Point.fromLngLat(MappingConstants.DEFAULT_LONGITUDE, MappingConstants.DEFAULT_LATITUDE),
    val cameraZoom: Double = DEFAULT_MAP_ZOOM_LEVEL,
):Parcelable
