package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.parcelize.Parcelize

@Stable
@Immutable
@Parcelize
data class CameraState(
    val cameraPosition: LatLng = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE),
    val cameraZoom: Double = DEFAULT_MAP_ZOOM_LEVEL,
):Parcelable
