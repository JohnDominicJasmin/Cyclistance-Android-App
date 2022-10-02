package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import android.animation.ValueAnimator
import android.location.Location
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import com.example.cyclistance.R
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils.*
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import timber.log.Timber



val locations = listOf(
    Point.fromLngLat(120.984222,14.599512),
    Point.fromLngLat(121.252176,14.628978),
    Point.fromLngLat(121.429968, 14.208007),
    Point.fromLngLat(121.269195, 13.823594),
    Point.fromLngLat(121.747392, 14.044194),
    Point.fromLngLat(125.037057, 6.523497),
    Point.fromLngLat(125.043330, 6.531999),
    Point.fromLngLat(125.224905, 6.887962),
    Point.fromLngLat(125.283468, 7.118415),
)





@Composable
fun MappingMapsScreen(
    state: MappingState,
    isDarkTheme: Boolean,
    mapUiComponents: MapUiComponents,
    modifier: Modifier,
    onNewLocationResult: (Location) -> Unit) {


    val context = LocalContext.current

    val navigationLocationProvider by rememberNavigationLocalProvider()
    var enhanceLocation by rememberSaveableLocation()


    val mapView = rememberMapView(context = context)

    val mapboxMap = remember {
        mapView.getMapboxMap()
    }


    val mapboxNavigation = remember {

        if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            mapUiComponents.navigationOptions?.let(MapboxNavigationProvider::create)
        }


    }

    LaunchedEffect(key1 = state.locationPermissionGranted) {
        if (state.locationPermissionGranted) {
            mapboxNavigation?.startTripSession()
        }
    }
    val locationObserver = remember {

        object : LocationObserver {
            override fun onNewRawLocation(rawLocation: Location) {
//                Timber.d("onNewRawLocation: $rawLocation")
            }

            override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
                val locationChange = with(locationMatcherResult.enhancedLocation) {
                    enhanceLocation.latitude != latitude && enhanceLocation.longitude != longitude
                }

                if (locationChange) {
                    enhanceLocation = locationMatcherResult.enhancedLocation
                    onNewLocationResult(enhanceLocation)
                }


                navigationLocationProvider.changePosition(
                    location = locationMatcherResult.enhancedLocation,
                    keyPoints = locationMatcherResult.keyPoints,
                    latLngTransitionOptions = mapUiComponents.transitionOptions,
                    bearingTransitionOptions = mapUiComponents.transitionOptions
                )

            }
        }
    }
    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

                mapView.location.apply {
                    setLocationProvider(navigationLocationProvider)
                    enabled = true
                }
                mapboxMap.loadStyleUri(if (isDarkTheme) Style.DARK else Style.MAPBOX_STREETS)
                mapboxNavigation.apply {
                    this?.registerLocationObserver(locationObserver)
                }

                locations.forEach {
                    val annotationApi = mapView.annotations
                    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                    val pointAnnotationOptions = mapUiComponents.pointAnnotationOptions
                        .withPoint(it)
                        .withIconImage(context.getDrawable(R.drawable.ic_arrow)?.toBitmap() ?: return@forEach)
                    pointAnnotationManager.create(pointAnnotationOptions)
                }
            }

            Lifecycle.Event.ON_START -> {
                Timber.v("Lifecycle Event: ON_START")
                mapView.onStart()
            }

            Lifecycle.Event.ON_RESUME -> {
                Timber.v("Lifecycle Event: ON_RESUME")
            }

            Lifecycle.Event.ON_PAUSE -> {
                Timber.v("Lifecycle Event: ON_PAUSE")
            }

            Lifecycle.Event.ON_STOP -> {
                Timber.v("Lifecycle Event: ON_STOP")
                mapView.onStop()
            }

            Lifecycle.Event.ON_DESTROY -> {
                Timber.v("Lifecycle Event: ON_DESTROY")
                mapView.onDestroy()
                mapboxNavigation?.stopTripSession()
                mapboxNavigation?.unregisterLocationObserver(locationObserver)
                mapboxNavigation?.onDestroy()

            }


            else -> {}
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView

        }, update = { updatedMapView ->


        })


}


