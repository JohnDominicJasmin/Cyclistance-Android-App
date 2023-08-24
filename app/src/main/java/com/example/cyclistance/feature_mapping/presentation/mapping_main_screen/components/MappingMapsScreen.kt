package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.ICON_LAYER_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ICON_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_LAYER_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.TRANSACTION_ICON_ID
import com.example.cyclistance.core.utils.formatter.IconFormatter.getHazardousLaneImage
import com.example.cyclistance.core.utils.formatter.IconFormatter.getNearbyCyclistImage
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.setDefaultSettings
import com.example.cyclistance.navigation.IsDarkTheme
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("MissingPermission")
@Suppress("Deprecation")
@Composable
fun MappingMapsScreen(
    modifier: Modifier,
    state: MappingState,
    uiState: MappingUiState,
    mapboxMap: MapboxMap?,
    hasTransaction: Boolean,
    isNavigating: Boolean,
    routeDirection: RouteDirection?,
    isRescueCancelled: Boolean,
    hazardousLaneMarkers: List<HazardousLaneMarker>,
    event: (MappingUiEvent) -> Unit
//    requestNavigationCameraToOverview: () -> Unit, //todo use this one
) {


    val context = LocalContext.current
    val nearbyCyclist = state.nearbyCyclist?.users


    val nearbyUserMarkers = remember { mutableStateListOf<Marker>() }

    val dismissNearbyUserMarkers = remember(mapboxMap) {
        {
            nearbyUserMarkers.apply {
                forEach { mapboxMap?.removeMarker(it) }
                clear()
            }
        }
    }

    val showNearbyCyclistsIcon = remember(nearbyCyclist, mapboxMap) {
        {
            dismissNearbyUserMarkers()
            nearbyCyclist?.filter {
                it.id != state.user.id
            }?.filter {
                it.isUserNeedHelp() == true
            }?.forEach { cyclist ->
                Timber.v("Cyclist Name: ${cyclist.name}")
                val location = cyclist.location
                val latitude = location?.latitude ?: return@forEach
                val longitude = location.longitude ?: return@forEach
                val description = cyclist.getDescription()
                val iconImage = description?.getNearbyCyclistImage(context)
                    ?.toBitmap(width = 120, height = 120)
                iconImage?.let { bitmap ->
                    mapboxMap ?: return@let
                    val icon = IconFactory.getInstance(context).fromBitmap(bitmap)
                    val markerOptions = MarkerOptions().apply {
                        setIcon(icon)
                        position(LatLng(latitude, longitude))
                        title = cyclist.id
                        this.snippet = MarkerSnippet.NearbyCyclistSnippet.type
                    }
                    val addedMarker = mapboxMap.addMarker(markerOptions)
                    addedMarker.let { nearbyUserMarkers.add(it) }
                }
            }
        }
    }

    val hazardousMarkers = remember { mutableStateListOf<Marker>() }


    val dismissHazardousMarkers = remember(mapboxMap) {
        {
            hazardousMarkers.apply {
                forEach { mapboxMap?.removeMarker(it) }
                clear()
            }
        }
    }
    val showHazardousLaneIcon = remember(hazardousLaneMarkers.size, mapboxMap, state.userLocation) {
        {

            dismissHazardousMarkers()
            hazardousLaneMarkers.filter { marker ->
                val markerLocation = LatLng(marker.latitude!!, marker.longitude!!)
                val userLocation = LatLng(
                    state.getCurrentLocation()?.latitude!!,
                    state.getCurrentLocation()?.longitude!!
                )
                markerLocation.distanceTo(userLocation) < MappingConstants.DEFAULT_RADIUS
            }.forEach { marker ->
                val iconImage =
                    marker.label.getHazardousLaneImage(
                        context = context,
                        isMarkerYours = marker.idCreator == state.userId)
                        ?.toBitmap(width = 120, height = 120)
                val latitude = marker.latitude ?: return@forEach
                val longitude = marker.longitude ?: return@forEach
                iconImage?.let { bitmap ->
                    mapboxMap ?: return@let
                    val icon = IconFactory.getInstance(context).fromBitmap(bitmap)
                    val markerOptions = MarkerOptions().apply {
                        setIcon(icon)
                        position(LatLng(latitude, longitude))
                        title = marker.id
                        snippet = MarkerSnippet.HazardousLaneSnippet.type

                    }
                    val addedMarker = mapboxMap.addMarker(markerOptions)
                    addedMarker.let { hazardousMarkers.add(it) }
                }
            }
        }
    }


    val hasActiveTransaction = remember(hasTransaction, isRescueCancelled) {
        hasTransaction || isRescueCancelled
    }

    val isUserNavigating = remember(key1 = isNavigating, key2 = routeDirection?.geometry) {
        val geometry = routeDirection?.geometry
        isNavigating || geometry?.isNotEmpty() == true
    }

    val shouldDismissIcons =
        remember(nearbyCyclist, isUserNavigating, hasActiveTransaction) {
            isUserNavigating || hasActiveTransaction

        }



    LaunchedEffect(key1 = state.mapType, key2 = shouldDismissIcons){
        if (state.mapType == MapType.HazardousLane.type) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

        if (shouldDismissIcons) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

    }

    LaunchedEffect(key1 = nearbyCyclist, key2 = state.mapType) {

        if (state.mapType == MapType.HazardousLane.type) {
            return@LaunchedEffect
        }

        if (shouldDismissIcons) {
            return@LaunchedEffect
        }

        showNearbyCyclistsIcon()
    }

    LaunchedEffect(
        key1 = shouldDismissIcons,
        key2 = state.mapType) {

        if (shouldDismissIcons) {
            dismissHazardousMarkers()
            return@LaunchedEffect
        }

        if (state.mapType == MapType.Default.type) {
            dismissHazardousMarkers()
            return@LaunchedEffect
        }
    }



    LaunchedEffect(key1 = hazardousLaneMarkers.size, key2 = state.userLocation, key3 = state.mapType) {

        val isLocationAvailable = state.userLocation?.latitude != null && state.userLocation.longitude != null

        if (!isLocationAvailable) {
            return@LaunchedEffect
        }

        if (shouldDismissIcons) {
            return@LaunchedEffect
        }

        if (state.mapType == MapType.Default.type) {
            return@LaunchedEffect
        }

        showHazardousLaneIcon()
    }


    LaunchedEffect(key1 = mapboxMap, uiState.isFabExpanded) {

        mapboxMap?.setOnMarkerClickListener {

            mapboxMap.animateCameraPosition(
                latLng = it.position,
                zoomLevel = MappingConstants.LOCATE_USER_ZOOM_LEVEL,
                cameraAnimationDuration = MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION)

            if (it.snippet == MarkerSnippet.HazardousLaneSnippet.type) {
                event(MappingUiEvent.HazardousLaneMarkerSelected(it.title))
            } else {
                event(MappingUiEvent.RescueeMarkerSelected(it.title))
            }
            true
        }

        mapboxMap?.addOnMapClickListener {
            event(MappingUiEvent.OnMapClick)
            true
        }

        mapboxMap?.addOnCameraMoveListener {
            if (uiState.isFabExpanded) {
                event(MappingUiEvent.OnCollapseExpandableFAB)
            }
        }

        mapboxMap?.addOnMapLongClickListener {
            event(MappingUiEvent.OnMapLongClick(it))
            true
        }

    }


    val clientLocation =
        remember(state.transactionLocation, state.rescuer?.location, state.rescuee?.location) {
            with(state) {
                transactionLocation ?: rescuer?.location ?: rescuee?.location
            }
        }

    val hasTransactionLocationChanges = remember(clientLocation) {
        clientLocation != null
    }

    val dismissTransactionLocationIcon = remember(mapboxMap) {
        {
            mapboxMap?.getStyle { style ->
                if (style.isFullyLoaded) {
                    style.removeImage(TRANSACTION_ICON_ID)
                    val geoJsonSource = style.getSourceAs<GeoJsonSource>(ICON_SOURCE_ID)
                    geoJsonSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
                }
            }
        }
    }

    val showTransactionLocationIcon = remember(mapboxMap, state.user) {
        { location: LocationModel ->
            dismissTransactionLocationIcon()
            val role = state.user.transaction?.role
            val mapIcon = if (role == Role.RESCUEE.name.lowercase()) {
                R.drawable.ic_map_rescuer
            } else {
                R.drawable.ic_map_rescuee
            }
            mapboxMap?.getStyle { style ->
                if (style.isFullyLoaded) {
                    val longitude = location.longitude ?: return@getStyle
                    val latitude = location.latitude ?: return@getStyle
                    style.removeImage(TRANSACTION_ICON_ID)
                    ContextCompat.getDrawable(context, mapIcon)?.toBitmap(width = 100, height = 100)
                        ?.let { iconBitmap ->
                            style.addImage(TRANSACTION_ICON_ID, iconBitmap)
                            val geoJsonSource = style.getSourceAs<GeoJsonSource>(ICON_SOURCE_ID)
                            val feature =
                                Feature.fromGeometry(Point.fromLngLat(longitude, latitude))
                            geoJsonSource?.setGeoJson(feature)
                        }
                }
            }
        }
    }

    LaunchedEffect(
        key1 = hasActiveTransaction,
        key2 = hasTransactionLocationChanges,
        key3 = clientLocation) {

        if (hasTransactionLocationChanges.not() || hasActiveTransaction.not()) {
            dismissTransactionLocationIcon()
            return@LaunchedEffect
        }

        clientLocation?.latitude ?: return@LaunchedEffect
        showTransactionLocationIcon(clientLocation)
    }

    Map(
        modifier = modifier,
        event = event)

}


@Composable
private fun Map(
    modifier: Modifier,
    event: (MappingUiEvent) -> Unit) {


    val isDarkTheme = IsDarkTheme.current
    val mapView = rememberMapViewWithLifecycle()
    var isInitialized by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()

    ) {


        AndroidView(factory = { mapView }) {
            if (isInitialized) {
                return@AndroidView
            }
            CoroutineScope(Dispatchers.Main).launch {
                Timber.v("Successfully recomposed in Map")


                val initSource = { loadedMapStyle: Style ->
                    loadedMapStyle.addSource(GeoJsonSource(ICON_SOURCE_ID))
                    loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
                }

                val initLayers = { loadedMapStyle: Style ->


                    val drawableIcon =
                        ContextCompat.getDrawable(it.context, R.drawable.ic_map_rescuer)
                    val bitmapIcon = drawableIcon?.toBitmap(width = 100, height = 100)
                    bitmapIcon?.let { loadedMapStyle.addImage(TRANSACTION_ICON_ID, it) }

                    loadedMapStyle.addLayer(
                        SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).apply {
                            setProperties(
                                iconImage(TRANSACTION_ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                            )
                        }
                    )


                    loadedMapStyle.addLayerBelow(
                        LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID).apply {
                            setProperties(
                                lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(5f),
                                lineColor(Color.parseColor("#006eff"))
                            )
                        }, ICON_LAYER_ID)


                }
                mapView.getMapAsync {
                    it.setStyle(if (isDarkTheme) Style.DARK else Style.LIGHT) { loadedStyle ->

                        if (loadedStyle.isFullyLoaded) {
                            event(MappingUiEvent.OnInitializeMap(it))
                            initSource(loadedStyle)
                            initLayers(loadedStyle)
                        }
                    }
                    it.setDefaultSettings()
                }
                isInitialized = true

            }
        }

    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.mapView
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }