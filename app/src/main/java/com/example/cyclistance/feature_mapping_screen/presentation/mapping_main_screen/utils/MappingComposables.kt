package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.MappingConstants
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
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
fun rememberNavigationLocalProvider() = remember {
    mutableStateOf(
        NavigationLocationProvider()
    )
}

@Composable
fun rememberSaveableLocation() = rememberSaveable {
    mutableStateOf(Location(MappingConstants.ENHANCE_LOCATION_PROVIDER))
}

@Composable
fun rememberMapView(context: Context) = remember {
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


            location2.apply {
                locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.navigation.R.drawable.mapbox_mylocation_bg_shape
                    ),
                    topImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.navigation.R.drawable.mapbox_mylocation_icon_default
                    ),
                    shadowImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.navigation.R.drawable.mapbox_user_icon_shadow
                    )
                )
                enabled = true
                pulsingEnabled = true
                this.showAccuracyRing = true
                this.pulsingColor = context.getColor(R.color.DodgerBlue)
                this.puckBearingEnabled = true
                this.pulsingMaxRadius = 120.0f
                this.puckBearingSource = PuckBearingSource.HEADING

            }
        }
    }
