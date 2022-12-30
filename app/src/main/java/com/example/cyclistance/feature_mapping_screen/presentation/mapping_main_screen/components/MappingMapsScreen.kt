package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_LAYER_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.validation.FormatterUtils.getMapIconImageDescription
import com.example.cyclistance.databinding.ActivityMappingBinding
import com.example.cyclistance.feature_mapping_screen.domain.model.Role
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.setDefaultSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import timber.log.Timber


@SuppressLint("MissingPermission")
@Suppress("Deprecation")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingMapsScreen(
    modifier: Modifier,
    state: MappingState,
    isDarkTheme: Boolean,
    userLocationAvailable: Boolean,
    mapMapView: MapView,
    mapboxMap: MapboxMap?,
    hasTransaction: Boolean,
    isRescueCancelled: Boolean,
    onInitializeMapboxMap: (MapboxMap) -> Unit,
    onInitializeMapView: (MapView) -> Unit,
    locationPermissionsState: MultiplePermissionsState?,
    onChangeCameraState: (LatLng, Double) -> Unit,
    onClickRescueeMapIcon:(String) -> Unit,
    requestNavigationCameraToOverview:() -> Unit,
    onMapClick: () -> Unit
    ) {


    val context = LocalContext.current



    val nearbyCyclists = remember(key1= state.nearbyCyclists?.users?.size) {
         state.nearbyCyclists?.users
    }


    LaunchedEffect(key1 = nearbyCyclists, key2 = mapboxMap){
        if(state.isNavigating){
            return@LaunchedEffect
        }

        if(isRescueCancelled.not() && hasTransaction){
            return@LaunchedEffect
        }

        nearbyCyclists?.filter{
            it.userAssistance?.needHelp == true
        }?.forEach { cyclist ->
                val location = cyclist.location
                val description = cyclist.userAssistance?.confirmationDetail?.description
                val iconImage = description?.getMapIconImageDescription(context)
                    ?.toBitmap(width = 120, height = 120)
                iconImage?.let { bitmap ->

                    val icon = IconFactory.getInstance(context).fromBitmap(bitmap)
                    val markerOptions = MarkerOptions()
                        .setIcon(icon)
                        .position(LatLng(location!!.latitude, location.longitude))
                        .setTitle(cyclist.id)

                    mapboxMap?.addMarker(markerOptions)
                    val firstLocation = nearbyCyclists.first().location ?: return@forEach
                    val lastLocation = nearbyCyclists.last().location ?: return@forEach
                    val latLngBound = LatLngBounds.Builder()
                        .include(LatLng(firstLocation.latitude, firstLocation.longitude))
                        .include(LatLng(lastLocation.latitude, lastLocation.longitude))
                        .build();
                    mapboxMap?.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBound, 200), 1000)
                }
            }


    }


    LaunchedEffect(key1 = mapboxMap){
        mapboxMap?.setOnMarkerClickListener {
            onClickRescueeMapIcon(it.title)
            true
        }
        mapMapView.setOnClickListener {
            onMapClick()
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


    LaunchedEffect(
        key1 = mapboxMap,
        key2 = hasTransactionLocationChanges,
        key3 = clientLocation) {

        val role = state.user.transaction?.role

        if (hasTransactionLocationChanges.not()) {
            return@LaunchedEffect
        }

        if(hasTransaction.not()){
            return@LaunchedEffect
        }

        if(isRescueCancelled){
            return@LaunchedEffect
        }
        val mapIcon = if(role == Role.RESCUEE.name.lowercase()){
            R.drawable.ic_map_rescuer
        }else{
            R.drawable.ic_map_rescuee
        }


        clientLocation?.latitude ?: return@LaunchedEffect

        val icon = IconFactory.getInstance(context).fromResource(mapIcon)
        val markerOptions = MarkerOptions()
            .icon(icon)
            .position(LatLng(clientLocation.latitude, clientLocation.longitude))
        mapboxMap?.addMarker(markerOptions)

    }

    LaunchedEffect(key1 = state.userRescueTransaction?.route, key2 = hasTransaction, key3 = isRescueCancelled){
        val transactionRoute = state.userRescueTransaction?.route
        val startingLocation = transactionRoute?.startingLocation
        val destinationLocation = transactionRoute?.destinationLocation


        if(hasTransaction.not()){
//          todo: mapboxNavigation.setNavigationRoutes(listOf()), Remove routes
            return@LaunchedEffect
        }

        if (isRescueCancelled) {
//          todo: mapboxNavigation.setNavigationRoutes(listOf()), Remove routes
            return@LaunchedEffect
        }

        startingLocation?.let {
            destinationLocation?.let {
                // TODO: Show route
          /*      mapboxNavigation.findRoute(context, originPoint = Point.fromLngLat(startingLocation.longitude, startingLocation.latitude),
                    destinationPoint = Point.fromLngLat(destinationLocation.longitude, destinationLocation.latitude)) {

                    mapboxNavigation.setNavigationRoutes(it)
                    requestNavigationCameraToOverview()
                }*/
            }
        }
    }







    AndroidViewBinding(factory = ActivityMappingBinding::inflate, modifier = modifier){
        val viewContext = this.root.context
        lateinit var _mapboxMap: MapboxMap
        val pixelDensity = Resources.getSystem().displayMetrics.density

        val initSource = { loadedMapStyle: Style ->
            loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID));
        }
        val initLayers = { loadedMapStyle: Style ->
            loadedMapStyle.addLayer(
                LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID).apply {
                    setProperties(
                        lineCap(Property.LINE_CAP_ROUND),
                        lineJoin(Property.LINE_JOIN_ROUND),
                        lineWidth(5f),
                        lineColor(Color.parseColor("#006eff"))
                    )
                }
            )

        }

        root.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {

                        Timber.v("Lifecycle Event: ON_CREATE")
                        mapView.getMapAsync {
                            _mapboxMap = it
                            it.setStyle(if (isDarkTheme) Style.TRAFFIC_NIGHT else Style.TRAFFIC_DAY) { loadedStyle ->
                                if(loadedStyle.isFullyLoaded) {
                                    onInitializeMapboxMap(_mapboxMap)
                                    onInitializeMapView(mapView)
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
                        val camera = mapboxMap?.cameraPosition ?: return@LifecycleEventObserver
                        val cameraCenter = camera.target
                        val cameraZoom =  camera.zoom
                        onChangeCameraState(cameraCenter, cameraZoom)
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

