package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
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
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ICON_SOURCE_ID
import com.myapp.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.myapp.cyclistance.core.utils.constants.MappingConstants.TRANSACTION_ICON_ID
import com.myapp.cyclistance.core.utils.formatter.IconFormatter.getHazardousLaneImage
import com.myapp.cyclistance.core.utils.formatter.IconFormatter.getNearbyCyclistImage
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.*
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.initLayers
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.initSource
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.setDefaultSettings
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.showRoute
import com.myapp.cyclistance.navigation.IsDarkTheme
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
                val locationAvailable = user.location?.latitude != null && user.location.longitude != null
                locationAvailable && user.isUserNeedHelp() == true
            }?.filter { user ->

                val markerLocation = LatLng(user.location?.latitude!!, user.location.longitude!!)
                val userLocation = LatLng(
                    state.userLocation?.latitude!!,
                    state.userLocation.longitude!!
                )
                markerLocation.distanceTo(userLocation) < MappingConstants.DEFAULT_RADIUS
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


    LaunchedEffect(key1 = state.defaultMapTypeSelected, key2 = shouldDismissIcons, key3 = mapboxMap) {

        if (!state.defaultMapTypeSelected) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

        if (shouldDismissIcons) {
            dismissNearbyUserMarkers()
            return@LaunchedEffect
        }

    }

    fun observeNearbyCyclistsIcon(){
        val userLocation = state.userLocation


        if (!state.defaultMapTypeSelected) {
            return
        }

        if (shouldDismissIcons) {
            return
        }

        if(uiState.searchingAssistance){
            return
        }

        if(userLocation == null){
            return
        }

        if(userLocation.latitude == null){
            return
        }

        if (userLocation.longitude == null){
            return
        }

        showNearbyCyclistsIcon()
    }

    LaunchedEffect(key1 = nearbyCyclist, key2 = state.userLocation, key3 = mapboxMap) {
        observeNearbyCyclistsIcon()
    }


    LaunchedEffect(key1 = nearbyCyclist, key2 = state.defaultMapTypeSelected, key3 = mapboxMap) {
        observeNearbyCyclistsIcon()
    }



    LaunchedEffect(
        key1 = shouldDismissIcons,
        key2 = state.hazardousMapTypeSelected,
        key3 = mapboxMap) {

        if (shouldDismissIcons) {
            dismissHazardousMarkers()
            return@LaunchedEffect
        }

        if (!state.hazardousMapTypeSelected) {
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

        if (!state.hazardousMapTypeSelected) {
            return
        }

        if(uiState.searchingAssistance){
            return
        }

        showHazardousLaneIcon()
    }

    LaunchedEffect(key1 = hazardousLaneMarkers.size, key2 = mapboxMap, key3 = state.hazardousMapTypeSelected) {
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
                    runCatching {
                        val geoJsonSource = style.getSourceAs<GeoJsonSource>(ICON_SOURCE_ID)
                        geoJsonSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
                    }.onFailure {
                        Timber.v("Mapbox style not loaded ${it.message}")
                    }

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
                            runCatching {
                                val geoJsonSource = style.getSourceAs<GeoJsonSource>(ICON_SOURCE_ID)
                                val feature =
                                    Feature.fromGeometry(Point.fromLngLat(longitude, latitude))
                                geoJsonSource?.setGeoJson(feature)
                            }.onFailure {
                                Timber.v("Mapbox style not loaded ${it.message}")
                            }
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

    LaunchedEffect(
        key1 = state.trafficMapTypeSelected,
        key2 = mapboxMap) {

        val geometry = uiState.routeDirection?.geometry
        val darkThemeMap = if (state.trafficMapTypeSelected) Style.TRAFFIC_NIGHT else Style.DARK
        val lightThemeMap = if (state.trafficMapTypeSelected) Style.TRAFFIC_DAY else Style.LIGHT

        mapboxMap?.setStyle(if (isDarkTheme) darkThemeMap else lightThemeMap) { loadedStyle ->

            if (!loadedStyle.isFullyLoaded) {
                return@setStyle
            }

            loadedStyle.initSource()
            loadedStyle.initLayers(context)

            if (geometry == null) {
                return@setStyle
            }

            loadedStyle.showRoute(geometry = geometry)
        }
    }
    Map(
        modifier = modifier,
        uiState = uiState,
        trafficMapTypeSelected = state.trafficMapTypeSelected,
        event = event)

}



@Composable
private fun Map(
    modifier: Modifier,
    uiState: MappingUiState,
    trafficMapTypeSelected: Boolean,
    event: (MappingUiEvent) -> Unit) {


    val isDarkTheme = IsDarkTheme.current
    val mapView = rememberMapViewWithLifecycle()
    var isInitialized by rememberSaveable {
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

                val geometry = uiState.routeDirection?.geometry
                val darkThemeMap = if (trafficMapTypeSelected) Style.TRAFFIC_NIGHT else Style.DARK
                val lightThemeMap = if (trafficMapTypeSelected) Style.TRAFFIC_DAY else Style.LIGHT


                mapView.getMapAsync { mapbox ->
                    mapbox.setStyle(if (isDarkTheme) darkThemeMap else lightThemeMap) { loadedStyle ->

                        if (!loadedStyle.isFullyLoaded) {
                            return@setStyle
                        }

                        event(MappingUiEvent.OnInitializeMap(mapbox))
                        loadedStyle.initSource()
                        loadedStyle.initLayers(view.context)

                        if (geometry == null) {
                            return@setStyle
                        }

                        loadedStyle.showRoute(geometry = geometry)

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