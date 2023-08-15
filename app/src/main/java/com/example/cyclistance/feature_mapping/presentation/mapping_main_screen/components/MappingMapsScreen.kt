package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants.ICON_LAYER_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ICON_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_LAYER_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.TRANSACTION_ICON_ID
import com.example.cyclistance.core.utils.validation.FormatterUtils.getMapIconImageDescription
import com.example.cyclistance.databinding.ActivityMappingBinding
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteDirection
import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.setDefaultSettings
import com.example.cyclistance.navigation.IsDarkTheme
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
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
    event: (MappingUiEvent) -> Unit
//    requestNavigationCameraToOverview: () -> Unit, //todo use this one
) {


    val context = LocalContext.current


    val nearbyCyclists = remember(state.nearbyCyclists?.users?.size, mapboxMap) {
        state.nearbyCyclists?.users
    }
    val dismissNearbyCyclistsIcon = remember(mapboxMap) {
        {
            mapboxMap?.removeAnnotations()
        }
    }

    val showNearbyCyclistsIcon = remember(nearbyCyclists, mapboxMap) {
        {
            dismissNearbyCyclistsIcon()

            nearbyCyclists?.filter{
                it.id != state.user.id
            }?.filter {
                it.isUserNeedHelp() == true
            }?.forEach { cyclist ->
                val location = cyclist.location
                val latitude = location?.latitude ?: return@forEach
                val longitude = location.longitude ?: return@forEach
                val description = cyclist.getDescription()
                val iconImage = description?.getMapIconImageDescription(context)
                    ?.toBitmap(width = 120, height = 120)
                iconImage?.let { bitmap ->
                    mapboxMap ?: return@let
                    val icon = IconFactory.getInstance(context).fromBitmap(bitmap)
                    MarkerOptions().apply {
                        setIcon(icon)
                        position(LatLng(latitude, longitude))
                        title = cyclist.id
                    }.also(mapboxMap::addMarker)
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

    val shouldDismissNearbyIcons = remember(nearbyCyclists, isUserNavigating, hasActiveTransaction) {
        isUserNavigating || hasActiveTransaction
    }
    LaunchedEffect(key1 = shouldDismissNearbyIcons, key2 = mapboxMap, key3= nearbyCyclists) {

        if (shouldDismissNearbyIcons) {
            dismissNearbyCyclistsIcon()
            return@LaunchedEffect
        }

        showNearbyCyclistsIcon()
    }


    LaunchedEffect(key1 = mapboxMap, uiState.isFabExpanded) {

        mapboxMap?.setOnMarkerClickListener {
            event(MappingUiEvent.RescueeMapIconSelected(it.title))
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
            event(MappingUiEvent.OnMapLongClick)
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
    Box(modifier = modifier) {

        AndroidViewBinding(
            factory = ActivityMappingBinding::inflate,
            modifier = Modifier.fillMaxSize()) {
            val viewContext = this.root.context
            var mapboxMap: MapboxMap? = null

            val initSource = { loadedMapStyle: Style ->
                loadedMapStyle.addSource(GeoJsonSource(ICON_SOURCE_ID))
                loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
            }

            val initLayers = { loadedMapStyle: Style ->


                val drawableIcon = ContextCompat.getDrawable(viewContext, R.drawable.ic_map_rescuer)
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


            root.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(
                LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_CREATE -> {

                            Timber.v("Lifecycle Event: ON_CREATE")
                            mapView.getMapAsync {


                                it.setStyle(if (isDarkTheme) Style.DARK else Style.LIGHT) { loadedStyle ->

                                    if (loadedStyle.isFullyLoaded) {
                                        event(MappingUiEvent.OnInitializeMap(it))
                                        mapboxMap = it
                                        initSource(loadedStyle)
                                        initLayers(loadedStyle)
                                    }
                                }
                                it.setDefaultSettings()
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
                            val camera = mapboxMap?.cameraPosition
                            val cameraCenter = camera?.target
                            val cameraZoom = camera?.zoom
                            cameraCenter?.let {
                                cameraZoom?.let {
                                    val cameraMoved =
                                        cameraCenter.latitude != 0.0 && cameraCenter.longitude != 0.0 && cameraZoom != 3.0

                                    if (!cameraMoved) {
                                        return@let
                                    }


                                    /*  event(
                                          MappingUiEvent.OnChangeCameraState(
                                              cameraState = CameraState(
                                                  position = cameraCenter,
                                                  zoom = cameraZoom)))*/
                                }
                            }
                        }

                        Lifecycle.Event.ON_STOP -> {
                            Timber.v("Lifecycle Event: ON_STOP")

                            mapView.onStop()
                        }

                        Lifecycle.Event.ON_DESTROY -> {

                            mapView.onDestroy()
                        }

                        else -> {}

                    }
                }
            )
        }
    }
}

