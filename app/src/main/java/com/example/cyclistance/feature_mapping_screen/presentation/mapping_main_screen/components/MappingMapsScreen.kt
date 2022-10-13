package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.ComposableLifecycle
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.startServiceIntentAction
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.location2
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





@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingMapsScreen(
    state: MappingState,
    isDarkTheme: Boolean,
    mapView: MapView,
    mapboxMap: MapboxMap,
    locationPermissionState: MultiplePermissionsState?,
    modifier: Modifier) {


    val context = LocalContext.current
    ComposableLifecycle { _, event ->
        when (event) {

            Lifecycle.Event.ON_CREATE -> {
                Timber.v("Lifecycle Event: ON_CREATE")
                mapView.location2.apply {
                    val pulsingEnabled = state.isSearchingForAssistance.and(locationPermissionState?.allPermissionsGranted == true)
                    this.pulsingEnabled = pulsingEnabled
                }

                mapboxMap.loadStyleUri(if (isDarkTheme) Style.DARK else Style.MAPBOX_STREETS)

                /*    locations.forEach {
                        val annotationApi = mapView.annotations
                        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                        val pointAnnotationOptions = mapUiComponents.pointAnnotationOptions
                            .withPoint(it)
                            .withIconImage(AppCompatResources.getDrawable(context, R.drawable.ic_arrow)?.toBitmap() ?: return@forEach)
                        pointAnnotationManager.create(pointAnnotationOptions)
                    }*/
            }

            Lifecycle.Event.ON_START -> {
                Timber.v("Lifecycle Event: ON_START")
            }

            Lifecycle.Event.ON_RESUME -> {
                Timber.v("Lifecycle Event: ON_RESUME")
            }

            Lifecycle.Event.ON_PAUSE -> {
                Timber.v("Lifecycle Event: ON_PAUSE")
            }

            Lifecycle.Event.ON_STOP -> {
                Timber.v("Lifecycle Event: ON_STOP")
            }

            Lifecycle.Event.ON_DESTROY -> {
                Timber.v("Lifecycle Event: ON_DESTROY")
                context.startServiceIntentAction(intentAction = MappingConstants.ACTION_STOP)

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


