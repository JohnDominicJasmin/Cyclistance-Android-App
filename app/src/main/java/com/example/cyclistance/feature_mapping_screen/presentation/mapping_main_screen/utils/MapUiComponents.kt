package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.animation.ValueAnimator
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

data class MapUiComponents(
    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
)
