package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import com.example.cyclistance.R
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
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location2
import timber.log.Timber






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

    val pulsingEnabled by derivedStateOf {
        state.isSearchingForAssistance.and(locationPermissionState?.allPermissionsGranted == true)
    }
    val annotationApi = remember(mapView) { mapView.annotations }
    val pointAnnotationManager = remember(annotationApi) { annotationApi.createPointAnnotationManager() }
    val pointAnnotationOptions = remember (true) { PointAnnotationOptions()
        .withIconImage(AppCompatResources.getDrawable(context, com.mapbox.maps.R.drawable.mapbox_user_icon)?.toBitmap() ?: return) }



    val nearbyCyclists by derivedStateOf {
        state.nearbyCyclists.activeUsers
    }

    LaunchedEffect(key1 = nearbyCyclists){
        nearbyCyclists.forEach {
            val latitude = it.location?.lat?.toDouble()
            val longitude = it.location?.lng?.toDouble()


            pointAnnotationOptions.withPoint(Point.fromLngLat(longitude ?: return@forEach, latitude ?: return@forEach))
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    LaunchedEffect(key1 = pulsingEnabled){
        mapView.location2.pulsingEnabled = pulsingEnabled
    }

    val mapStyle by derivedStateOf {
        if (isDarkTheme) Style.DARK else Style.OUTDOORS
    }
    LaunchedEffect(key1 = mapStyle){
        mapboxMap.loadStyleUri(mapStyle)
    }

    ComposableLifecycle { _, event ->
        when (event) {

            Lifecycle.Event.ON_CREATE -> {
                Timber.v("Lifecycle Event: ON_CREATE")

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


