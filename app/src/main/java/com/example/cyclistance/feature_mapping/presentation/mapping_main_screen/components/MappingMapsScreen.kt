package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.annotation.SuppressLint
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
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.ICON_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.TRANSACTION_ICON_ID
import com.example.cyclistance.core.utils.formatter.IconFormatter.getHazardousLaneImage
import com.example.cyclistance.core.utils.formatter.IconFormatter.getNearbyCyclistImage
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.initLayers
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.initSource
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
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

    val showNearbyCyclistsIcon = remember(nearbyCyclist, mapboxMap,state.userLocation?.latitude) {
        {
            dismissNearbyUserMarkers()
            nearbyCyclist?.filter {
                it.id != state.user.id
            }?.filter {user ->

                user.isUserNeedHelp() == true
            }?.filter { user ->

                val markerLocation = LatLng(user.location?.latitude!!, user.location.longitude!!)
                val userLocation = LatLng(
                    state.userLocation?.latitude!!,
                    state.userLocation.longitude!!
                )
                markerLocation.distanceTo(userLocation)< MappingConstants.DEFAULT_RADIUS
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
                mapboxMap ?: return@forEach
                val latitude = marker.latitude ?: return@forEach
                val longitude = marker.longitude ?: return@forEach
                val iconImage =
                    marker.label.getHazardousLaneImage(
                        context = context,
                        isMarkerYours = marker.idCreator == state.userId)
                        ?.toBitmap(width = 120, height = 120)

                iconImage?.let { bitmap ->

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


    val hasActiveTransaction = remember(uiState.hasTransaction, uiState.isRescueCancelled) {
        uiState.hasTransaction || uiState.isRescueCancelled
    }

    val isUserNavigating = remember(key1 = uiState.isNavigating, key2 = uiState.routeDirection?.geometry) {
        val geometry = uiState.routeDirection?.geometry
        uiState.isNavigating || geometry?.isNotEmpty() == true
    }

    val shouldDismissIcons =
        remember(nearbyCyclist, isUserNavigating, hasActiveTransaction) {
            isUserNavigating || hasActiveTransaction

        }
    val dismissibleNearbyUserMapTypes =
        remember { listOf(MapType.HazardousLane.type, MapType.Traffic.type) }
    val dismissibleHazardousMapTypes =
        remember { listOf(MapType.Default.type, MapType.Traffic.type) }


    LaunchedEffect(key1 = state.mapType, key2 = shouldDismissIcons, key3 = mapboxMap) {

        if (state.mapType in dismissibleNearbyUserMapTypes) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

        if (shouldDismissIcons) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

    }

    fun observeNearbyCyclistsIcon(){
        if (state.mapType in dismissibleNearbyUserMapTypes) {
            return
        }

        if (shouldDismissIcons) {
            return
        }

        if(uiState.searchingAssistance){
            return
        }

        if(state.userLocation == null){
            return
        }

        showNearbyCyclistsIcon()
    }

    LaunchedEffect(key1 = nearbyCyclist, key2 = state.userLocation, key3 = mapboxMap) {
        observeNearbyCyclistsIcon()
    }


    LaunchedEffect(key1 = nearbyCyclist, key2 = state.mapType, key3 = mapboxMap) {
        observeNearbyCyclistsIcon()
    }



    LaunchedEffect(
        key1 = shouldDismissIcons,
        key2 = state.mapType,
        key3 = mapboxMap) {

        if (shouldDismissIcons) {
            dismissHazardousMarkers()
            return@LaunchedEffect
        }

        if (state.mapType in dismissibleHazardousMapTypes) {
            dismissHazardousMarkers()
            return@LaunchedEffect
        }
    }


   fun observeHazardousMarker() {
        val isLocationAvailable = state.userLocation?.latitude != null && state.userLocation.longitude != null

        if (!isLocationAvailable) {
            return
        }

        if (shouldDismissIcons) {
            return
        }

        if (state.mapType in dismissibleHazardousMapTypes) {
            return
        }

        if(uiState.searchingAssistance){
            return
        }

        showHazardousLaneIcon()
    }

    LaunchedEffect(key1 = hazardousLaneMarkers.size, key2 = mapboxMap, key3 = state.mapType) {
        observeHazardousMarker()
    }


    LaunchedEffect(key1 = mapboxMap, key2 = state.userLocation) {
        observeHazardousMarker()
    }

    LaunchedEffect(key1 = mapboxMap, uiState.isFabExpanded) {

        mapboxMap?.setOnMarkerClickListener {

            mapboxMap.animateCameraPosition(
                latLng = it.position,
                zoomLevel = LOCATE_USER_ZOOM_LEVEL,
                cameraAnimationDuration = DEFAULT_CAMERA_ANIMATION_DURATION)
            event(MappingUiEvent.OnClickMapMarker(markerSnippet = it.snippet, markerId = it.title))
            true
        }

        mapboxMap?.addOnMapClickListener {
            event(MappingUiEvent.OnMapClick)
            true
        }

        mapboxMap?.addOnCameraMoveListener {
            if (uiState.isFabExpanded) {
                event(MappingUiEvent.ExpandableFab(false))
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

    val transactionLocationChanges = remember(clientLocation) {
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
            val role = state.user.getRole()
            val mapIcon = if (role == Role.Rescuee.name) {
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
        key2 = transactionLocationChanges,
        key3 = clientLocation) {

        if (transactionLocationChanges.not() || hasActiveTransaction.not()) {
            dismissTransactionLocationIcon()
            return@LaunchedEffect
        }

        clientLocation?.latitude ?: return@LaunchedEffect
        showTransactionLocationIcon(clientLocation)
    }
    val isDarkTheme = IsDarkTheme.current
    LaunchedEffect(key1 = state.mapType, key2 = mapboxMap) {
        val darkThemeMap =
            if (state.mapType == MapType.Traffic.type) Style.TRAFFIC_NIGHT else Style.DARK
        val lightThemeMap =
            if (state.mapType == MapType.Traffic.type) Style.TRAFFIC_DAY else Style.LIGHT

        mapboxMap?.setStyle(if (isDarkTheme) darkThemeMap else lightThemeMap){ loadedStyle ->

            if (loadedStyle.isFullyLoaded) {
                loadedStyle.initSource()
                loadedStyle.initLayers(context)
            }
        }
    }
    Map(
        modifier = modifier,
        mapType = state.mapType,
        event = event)

}



@Composable
private fun Map(
    modifier: Modifier,
    mapType: String,
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


        AndroidView(factory = { mapView }) { view ->
            if (isInitialized) {
                return@AndroidView
            }
            CoroutineScope(Dispatchers.Main).launch {

                val darkThemeMap =
                    if (mapType == MapType.Traffic.type) Style.TRAFFIC_NIGHT else Style.DARK
                val lightThemeMap =
                    if (mapType == MapType.Traffic.type) Style.TRAFFIC_DAY else Style.LIGHT
                mapView.getMapAsync { mapbox ->
                    mapbox.setStyle(if (isDarkTheme) darkThemeMap else lightThemeMap) { loadedStyle ->

                        if (loadedStyle.isFullyLoaded) {
                            event(MappingUiEvent.OnInitializeMap(mapbox))
                            loadedStyle.initSource()
                            loadedStyle.initLayers(view.context)

                        }
                    }
                    mapbox.setDefaultSettings()
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