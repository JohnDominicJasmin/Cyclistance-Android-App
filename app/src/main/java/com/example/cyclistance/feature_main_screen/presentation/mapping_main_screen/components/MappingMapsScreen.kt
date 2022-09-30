package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import com.example.cyclistance.R
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils.ComposableLifecycle
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils.rememberLocation
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils.rememberMapView
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.utils.rememberNavigationLocalProvider
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import timber.log.Timber


@Composable
fun MappingMapsScreen(isDarkTheme: LiveData<Boolean?>, modifier: Modifier) {
    val context = LocalContext.current

    val navigationLocationProvider by rememberNavigationLocalProvider()
    var enhanceLocation by rememberLocation()

    val locationObserver = remember {

        object : LocationObserver {
            override fun onNewRawLocation(rawLocation: Location) {
//                Timber.d("onNewRawLocation: $rawLocation")
            }

            override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
                val isMoving = with(locationMatcherResult.enhancedLocation) {
                    enhanceLocation.latitude != latitude && enhanceLocation.longitude != longitude
                }

                if (isMoving) {
                    enhanceLocation = locationMatcherResult.enhancedLocation
                    navigationLocationProvider.changePosition(
                        locationMatcherResult.enhancedLocation,
                        locationMatcherResult.keyPoints
                    )
                }


            }
        }
    }

    val mapView = rememberMapView(context = context)

    val mapboxMap = remember {
        mapView.getMapboxMap()
    }


    val mapboxNavigation = remember {

        if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(mapView.context)
                    .accessToken(mapView.context.getString(R.string.MapsDownloadToken))
                    .build())
        }


    }

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Timber.v("Lifecycle Event: ON_CREATE")
                mapView.location.apply {
                    setLocationProvider(navigationLocationProvider)
                    enabled = true
                }
                mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
                mapboxNavigation.apply {
                    this.startTripSession()
                    this.registerLocationObserver(locationObserver)
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
                mapboxNavigation.stopTripSession()
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.onDestroy()

            }


            else -> {}
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView

        }, update = { updatedMapView ->

            val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
            updatedMapView.camera.easeTo(
                CameraOptions.Builder()
// Centers the camera to the lng/lat specified.
                    .center(Point.fromLngLat(enhanceLocation.longitude, enhanceLocation.latitude))
// specifies the zoom value. Increase or decrease to zoom in or zoom out
                    .zoom(6.0)
// specify frame of reference from the center.
                    .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
                    .build(),
                animationOptions = mapAnimationOptions
            )


        })


}


