package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils

import android.animation.ValueAnimator
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.navigation.base.options.NavigationOptions

data class MapUiComponents(
    val locationEngine: LocationEngine? = null,
    val locationEngineRequest: LocationEngineRequest? = null,
    val navigationOptions: NavigationOptions? = null,
    val transitionOptions: ValueAnimator.() -> Unit = {},
    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()


)
