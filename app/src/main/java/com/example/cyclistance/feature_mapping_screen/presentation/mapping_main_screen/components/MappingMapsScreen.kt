package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.databinding.ActivityMappingBinding
import com.example.cyclistance.feature_mapping_screen.domain.model.Role
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.getMapIconImageDescription
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.setDefaultSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.formatter.UnitType
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.NavigationStyles
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.model.*
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingMapsScreen(
    modifier: Modifier,
    state: MappingState,
    isDarkTheme: Boolean,
    mapsMapView: MapView,
    hasTransaction: Boolean,
    isRescueCancelled: Boolean,
    mapboxNavigation: MapboxNavigation,
    onInitializeMapView: (MapView) -> Unit,
    onInitializeNavigationCamera: (NavigationCamera) -> Unit,
    locationPermissionsState: MultiplePermissionsState?,
    onChangeCameraState: (Point, Double) -> Unit,
    onClickRescueeMapIcon:(String) -> Unit,
    onMapClick: () -> Unit
    ) {


    val context = LocalContext.current


    val nearbyCyclists by remember(key1= state.nearbyCyclists.users.size) {
        derivedStateOf { state.nearbyCyclists.users }
    }
    val pulsingEnabled by remember(state.isSearchingForAssistance, locationPermissionsState?.allPermissionsGranted) {
        derivedStateOf {
            state.isSearchingForAssistance.and(locationPermissionsState?.allPermissionsGranted == true)
        }
    }
    val annotationApi = remember(mapsMapView) { mapsMapView.annotations }
    val pointAnnotationManager = remember(annotationApi) { annotationApi.createPointAnnotationManager().apply{
        iconAllowOverlap = false
        iconIgnorePlacement = false
        textAllowOverlap = false
    }}


    LaunchedEffect(key1 = nearbyCyclists, key2 = mapsMapView){
        if(state.isNavigating){
            return@LaunchedEffect
        }

        if(isRescueCancelled.not() && hasTransaction){
            return@LaunchedEffect
        }

        pointAnnotationManager.deleteAll()
        nearbyCyclists
            .filter {
                it.id != state.user.id
            }.filter {
                it.userAssistance?.needHelp == true
            }.forEach { cyclist ->
                val location = cyclist.location
                val cyclistAssistance = cyclist.userAssistance
                val iconImage = cyclistAssistance?.getMapIconImageDescription(context)
                    ?.toBitmap(width = 120, height = 120)
                iconImage?.let { bitmap ->
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withIconImage(bitmap)
                        .withTextColor(context.getColor(R.color.Transparent))
                        .withTextField(cyclist.id!!)
                        .withPoint(Point.fromLngLat(location?.longitude ?: return@forEach, location.latitude))
                    pointAnnotationManager.create(pointAnnotationOptions)
                }
            }


    }


    LaunchedEffect(key1 = mapsMapView){

        mapsMapView.getMapboxMap().addOnMapClickListener{
            onMapClick()
            true
        }

        pointAnnotationManager.addClickListener{
            it.textField?.let { it1 -> onClickRescueeMapIcon(it1) }
            true
        }
    }


 val clientLocation = remember(state.transactionLocation, state.rescuer?.location, state.rescuee?.location){
        with(state){
             transactionLocation ?: rescuer?.location ?: rescuee?.location
        }
    }

    val hasTransactionLocationChanges = remember(clientLocation){
        clientLocation != null
    }

    LaunchedEffect(key1 = mapsMapView, key2 = hasTransactionLocationChanges, key3 = clientLocation){

        val role = state.user.transaction?.role

        if(hasTransactionLocationChanges.not()){
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

        val pointAnnotationOptions = PointAnnotationOptions()
            .withIconImage(
                AppCompatResources.getDrawable(context, mapIcon)
                    ?.toBitmap(width = 98, height = 98)!!)
            .withPoint(Point.fromLngLat(clientLocation!!.longitude, clientLocation.latitude))

        pointAnnotationManager.deleteAll()
        pointAnnotationManager.create(pointAnnotationOptions)

    }



    LaunchedEffect(key1 = pulsingEnabled, mapsMapView){
        mapsMapView.location2.pulsingEnabled = pulsingEnabled
    }

    AndroidViewBinding(factory = ActivityMappingBinding::inflate, modifier = modifier){
        val parentContext = this.root.context
        lateinit var mapboxMap: MapboxMap
        lateinit var navigationCamera: NavigationCamera
        lateinit var viewportDataSource: MapboxNavigationViewportDataSource
        val pixelDensity = Resources.getSystem().displayMetrics.density
        val overviewPadding: EdgeInsets by lazy {
            EdgeInsets(
                140.0 * pixelDensity,
                40.0 * pixelDensity,
                120.0 * pixelDensity,
                40.0 * pixelDensity
            )
        }
        val landscapeOverviewPadding: EdgeInsets by lazy {
            EdgeInsets(
                30.0 * pixelDensity,
                380.0 * pixelDensity,
                110.0 * pixelDensity,
                20.0 * pixelDensity
            )
        }
        val followingPadding: EdgeInsets by lazy {
            EdgeInsets(
                180.0 * pixelDensity,
                40.0 * pixelDensity,
                150.0 * pixelDensity,
                40.0 * pixelDensity
            )
        }
        val landscapeFollowingPadding: EdgeInsets by lazy {
            EdgeInsets(
                30.0 * pixelDensity,
                380.0 * pixelDensity,
                110.0 * pixelDensity,
                40.0 * pixelDensity
            )
        }
        lateinit var maneuverApi: MapboxManeuverApi

        lateinit var routeLineApi: MapboxRouteLineApi
        lateinit var routeLineView: MapboxRouteLineView
        val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
        lateinit var routeArrowView: MapboxRouteArrowView
        val navigationLocationProvider = NavigationLocationProvider()




        val locationObserver = object : LocationObserver {


            override fun onNewRawLocation(rawLocation: Location) {
            }

            override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
                val enhancedLocation = locationMatcherResult.enhancedLocation
                navigationLocationProvider.changePosition(
                    location = enhancedLocation,
                    keyPoints = locationMatcherResult.keyPoints,
                )

                viewportDataSource.onLocationChanged(enhancedLocation)
                viewportDataSource.evaluate()



            }
        }


        val routesObserver = RoutesObserver { routeUpdateResult ->
            if (routeUpdateResult.routes.isNotEmpty()) {
                val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

                routeLineApi.setRoutes(
                    routeLines
                ) { value ->
                    mapboxMap.getStyle()?.apply {
                        routeLineView.renderRouteDrawData(this, value)
                    }
                }

                viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
                viewportDataSource.evaluate()
            } else {
                val style = mapboxMap.getStyle()
                if (style != null) {
                    routeLineApi.clearRouteLine { value ->
                        routeLineView.renderClearRouteLineValue(
                            style,
                            value
                        )
                    }
                    routeArrowView.render(style, routeArrowApi.clearArrows())
                }

                viewportDataSource.clearRouteData()
                viewportDataSource.evaluate()
            }
        }






        root.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        Timber.v("Lifecycle Event: ON_CREATE")
                        mapboxMap = mapView.getMapboxMap().apply {
                            setBounds(
                                CameraBoundsOptions.Builder()
                                    .minZoom(MappingConstants.MIN_ZOOM_LEVEL_MAPS)
                                    .maxZoom(MappingConstants.MAX_ZOOM_LEVEL_MAPS)
                                    .build())
                        }
                        mapView.apply {
                            setDefaultSettings(parentContext, navigationLocationProvider)
                        }


                        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
                        navigationCamera = NavigationCamera(
                            mapboxMap,
                            mapView.camera,
                            viewportDataSource
                        )
                        mapView.camera.addCameraAnimationsLifecycleListener(
                            NavigationBasicGesturesHandler(navigationCamera)
                        )
                        if (parentContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            viewportDataSource.overviewPadding = landscapeOverviewPadding
                        } else {
                            viewportDataSource.overviewPadding = overviewPadding
                        }
                        if (parentContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            viewportDataSource.followingPadding = landscapeFollowingPadding
                        } else {
                            viewportDataSource.followingPadding = followingPadding
                        }


                        maneuverApi = MapboxManeuverApi(
                            MapboxDistanceFormatter(
                                DistanceFormatterOptions.Builder(context)
                                .unitType(UnitType.METRIC)
                                .build())
                        )





                        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(parentContext)
                            .displayRestrictedRoadSections(true)
                            .withRouteLineBelowLayerId(MappingConstants.ROAD_LABEL_NAVIGATION)
                            .build()

                        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
                        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

                        val routeArrowOptions = RouteArrowOptions.Builder(parentContext).build()
                        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

                        mapboxMap.loadStyleUri(
                            if (isDarkTheme) NavigationStyles.NAVIGATION_NIGHT_STYLE else NavigationStyles.NAVIGATION_DAY_STYLE
                        )


                        if(locationPermissionsState?.allPermissionsGranted == true) {
                            mapboxNavigation.startTripSession()
                        }

                        onInitializeMapView(mapView)
                        onInitializeNavigationCamera(navigationCamera)



                    }



                    Lifecycle.Event.ON_START -> {
                        Timber.v("Lifecycle Event: ON_START")
                        mapboxNavigation.registerRoutesObserver(routesObserver)
                        mapboxNavigation.registerLocationObserver(locationObserver)
                        mapView.onStart()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        Timber.v("Lifecycle Event: ON_RESUME")
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        Timber.v("Lifecycle Event: ON_PAUSE")
                        val cameraState = mapboxMap.cameraState
                        onChangeCameraState(cameraState.center, cameraState.zoom)
                    }
                    Lifecycle.Event.ON_STOP -> {
                        Timber.v("Lifecycle Event: ON_STOP")
                        mapboxNavigation.unregisterRoutesObserver(routesObserver)
                        mapboxNavigation.unregisterLocationObserver(locationObserver)
                        mapView.onStop()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        MapboxNavigationProvider.destroy()
                        maneuverApi.cancel()
                        routeLineApi.cancel()
                        routeLineView.cancel()
                        mapView.onDestroy()
                    }
                    else -> {}

                }
            }
        )




    }

}


