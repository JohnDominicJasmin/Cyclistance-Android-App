package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.cyclistance.core.utils.MappingConstants
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style

@Composable
fun MapScreen(isDarkTheme: LiveData<Boolean>, modifier: Modifier) {

    AndroidView(
        modifier = modifier,
        factory = { context ->

            MapView(context).apply {

                this.getMapAsync { mapboxMap ->
                    with(mapboxMap) {
                        this@apply.findViewTreeLifecycleOwner()?.let { lifeCycleOwner ->
                            isDarkTheme.observe(lifeCycleOwner) { darkTheme ->
                                setStyle(if (darkTheme) Style.TRAFFIC_NIGHT else Style.TRAFFIC_DAY)
                            }
                        }
                        uiSettings.isAttributionEnabled = false
                        uiSettings.isLogoEnabled = false
                        setMaxZoomPreference(MappingConstants.MAX_ZOOM_LEVEL_MAPS)
                        setMinZoomPreference(MappingConstants.MIN_ZOOM_LEVEL_MAPS)
                        animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(LatLng(MappingConstants.DEFAULT_LATITUDE, MappingConstants.DEFAULT_LONGITUDE))
                                    .zoom(MappingConstants.MAP_ZOOM)
                                    .tilt(MappingConstants.CAMERA_TILT_DEGREES)
                                    .build()), MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION)
                    }

                }

            }
        }
    )
}