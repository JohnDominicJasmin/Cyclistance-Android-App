package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants.MAX_ZOOM_LEVEL_MAPS
import com.example.cyclistance.core.utils.constants.MappingConstants.MIN_ZOOM_LEVEL_MAPS
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar

@Composable
inline fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    crossinline onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}




@Composable
fun rememberMapView(context: Context) = remember {
    lazy {
        MapView(
            context,
            mapInitOptions = MapInitOptions(
                context,
                resourceOptions = ResourceOptions
                    .Builder()
                    .accessToken(context.getString(R.string.MapsDownloadToken)).build())
        ).apply {
            id = R.id.mapView
            scalebar.enabled = false
            logo.enabled = false
            attribution.enabled = false
            getMapboxMap().setBounds(
                CameraBoundsOptions.Builder()
                    .minZoom(MIN_ZOOM_LEVEL_MAPS)
                    .maxZoom(MAX_ZOOM_LEVEL_MAPS)
                    .build()
            )

            location2.apply {

                locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.maps.R.drawable.mapbox_mylocation_bg_shape
                    ),
                    topImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.maps.R.drawable.mapbox_mylocation_icon_default
                    ),
                    shadowImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.maps.R.drawable.mapbox_user_icon_shadow
                    )
                )

                showAccuracyRing = true
                pulsingColor = ContextCompat.getColor(context, R.color.ThemeColor)
                puckBearingEnabled = false
                pulsingMaxRadius = 120.0f
                puckBearingSource = PuckBearingSource.HEADING

            }
        }
    }
    }
