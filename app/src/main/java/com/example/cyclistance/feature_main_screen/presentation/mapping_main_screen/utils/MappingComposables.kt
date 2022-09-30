package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.MappingConstants
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
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
fun rememberLocation() = remember {
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
        }
    }
