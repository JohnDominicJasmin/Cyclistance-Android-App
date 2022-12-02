package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.BUTTON_ANIMATION_DURATION
import com.example.cyclistance.databinding.ActivityMappingBinding
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Cancellation
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.formatter.UnitType
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
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
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingMapsScreen(
    state: MappingState,
    isDarkTheme: Boolean,
    mapsMapView: MapView,
    mapboxNavigation: MapboxNavigation,
    onInitializeMapView: (MapView) -> Unit,
    onInitializeNavigationCamera: (NavigationCamera) -> Unit,
    locationPermissionsState: MultiplePermissionsState?,
    onChangeCameraState: (Point, Double) -> Unit,
    modifier: Modifier) {


    val context = LocalContext.current


    val nearbyCyclists by remember(key1= state.nearbyCyclists.users) {
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

    LaunchedEffect( key1 = nearbyCyclists, key2 = mapsMapView, key3 = state.userRescueTransaction){

        val transactionCancelled = (state.userRescueTransaction?.cancellation ?: Cancellation()).rescueCancelled
        val transactionId = state.userRescueTransaction?.id

        if(transactionCancelled.not() && transactionId.isNullOrEmpty().not()){
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
                    ?.toBitmap(width = 100, height = 100)
                iconImage?.let {
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withIconImage(it)
                        .withPoint(
                            Point.fromLngLat(
                                location?.longitude ?: return@forEach,
                                location.latitude))
                    pointAnnotationManager.create(pointAnnotationOptions)
                }
            }
    }


    LaunchedEffect(key1 = mapsMapView, key2 = state.transactionLocation, key3 = state.userRescueTransaction){
        val rescueCancelled = (state.userRescueTransaction?.cancellation ?: Cancellation()).rescueCancelled
        val location = state.transactionLocation ?: state.rescuer.location
        val hasTransactionLocationChanges = state.userRescueTransaction != null && location != null

        if(rescueCancelled || hasTransactionLocationChanges.not()){
            return@LaunchedEffect
        }

        val pointAnnotationOptions = PointAnnotationOptions()
            .withIconImage(
                AppCompatResources.getDrawable(context, R.drawable.ic_navigation_map_icon)
                    ?.toBitmap(width = 90, height = 90)!!)
            .withPoint(Point.fromLngLat(location!!.longitude, location.latitude))

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
        lateinit var tripProgressApi: MapboxTripProgressApi
        lateinit var routeLineApi: MapboxRouteLineApi
        lateinit var routeLineView: MapboxRouteLineView
        val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
        lateinit var routeArrowView: MapboxRouteArrowView
        val navigationLocationProvider = NavigationLocationProvider()




        val clearRouteAndStopNavigation = {
            mapboxNavigation.setNavigationRoutes(listOf())

            soundButton.visibility = View.INVISIBLE
            maneuverView.visibility = View.INVISIBLE
            routeOverview.visibility = View.INVISIBLE
            tripProgressCard.visibility = View.INVISIBLE
        }
        val setRouteAndStartNavigation = { routes: List<NavigationRoute> ->
            mapboxNavigation.setNavigationRoutes(routes)

            soundButton.visibility = View.VISIBLE
            routeOverview.visibility = View.VISIBLE
            tripProgressCard.visibility = View.VISIBLE

            navigationCamera.requestNavigationCameraToOverview()
        }






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

        val routeProgressObserver = RouteProgressObserver { routeProgress ->
            viewportDataSource.onRouteProgressChanged(routeProgress)
            viewportDataSource.evaluate()

            val style = mapboxMap.getStyle()
            if (style != null) {
                val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
                routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
            }

            val maneuvers = maneuverApi.getManeuvers(routeProgress)
            maneuvers.fold(
                { error ->
                 Timber.e(error.errorMessage)
                },
                {
                    maneuverView.renderManeuvers(maneuvers)
                }
            )

            tripProgressView.render(
                tripProgressApi.getTripProgress(routeProgress)
            )
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

                        val distanceFormatterOptions = mapboxNavigation.navigationOptions.distanceFormatterOptions

                        maneuverApi = MapboxManeuverApi(
                            MapboxDistanceFormatter(
                                DistanceFormatterOptions.Builder(context)
                                .unitType(UnitType.METRIC)
                                .build())
                        )

                        tripProgressApi = MapboxTripProgressApi(
                            TripProgressUpdateFormatter.Builder(parentContext)
                                .distanceRemainingFormatter(
                                    DistanceRemainingFormatter(distanceFormatterOptions)
                                )
                                .timeRemainingFormatter(
                                    TimeRemainingFormatter(parentContext)
                                )
                                .percentRouteTraveledFormatter(
                                    PercentDistanceTraveledFormatter()
                                )
                                .estimatedTimeToArrivalFormatter(
                                    EstimatedTimeToArrivalFormatter(parentContext, TimeFormat.NONE_SPECIFIED)
                                )
                                .build()
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

                        stop.setOnClickListener {
                            clearRouteAndStopNavigation()
                        }
                        recenter.setOnClickListener {
                            navigationCamera.requestNavigationCameraToFollowing()
                            routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
                        }
                        routeOverview.setOnClickListener {
                            navigationCamera.requestNavigationCameraToOverview()
                            recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
                        }

                        soundButton.unmute()

                        if(locationPermissionsState?.allPermissionsGranted == true) {
                            mapboxNavigation.startTripSession()
                        }

                        onInitializeMapView(mapView)
                        onInitializeNavigationCamera(navigationCamera)

                        tripProgressCard.visibility = state.tripProgressCardVisibility
                        maneuverView.visibility = state.maneuverViewVisibility
                        soundButton.visibility = state.soundButtonVisibility
                        routeOverview.visibility = state.routeOverviewVisibility
                        recenter.visibility = state.recenterButtonVisibility
                    }



                    Lifecycle.Event.ON_START -> {
                        mapboxNavigation.registerRoutesObserver(routesObserver)
                        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
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
                        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
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


