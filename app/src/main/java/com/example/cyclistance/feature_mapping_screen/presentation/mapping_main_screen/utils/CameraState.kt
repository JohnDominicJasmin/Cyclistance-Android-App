package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize

@Stable
@Immutable
@Parcelize
data class CameraState(
    val cameraPosition: Point? = null,
    val cameraZoom: Double? = null,
):Parcelable
