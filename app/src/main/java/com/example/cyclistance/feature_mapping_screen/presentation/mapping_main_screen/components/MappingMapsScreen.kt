package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.BUTTON_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.databinding.ActivityMappingBinding
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
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
    mapsMapboxMap: MapboxMap,
    onInitializeMapView: (MapView) -> Unit,
    locationPermissionsState: MultiplePermissionsState?,
    modifier: Modifier) {


    val context = LocalContext.current


    val nearbyCyclists by remember(key1= state.nearbyCyclists.activeUsers.size) {
        derivedStateOf { state.nearbyCyclists.activeUsers }
    }
    val pulsingEnabled by remember(state.isSearchingForAssistance, locationPermissionsState?.allPermissionsGranted) {
        derivedStateOf {
            state.isSearchingForAssistance.and(locationPermissionsState?.allPermissionsGranted == true)
        }
    }
    val annotationApi = remember(mapsMapView) { mapsMapView.annotations }
    val pointAnnotationManager = remember(annotationApi) { annotationApi.createPointAnnotationManager() }

    LaunchedEffect(key1 = nearbyCyclists){
        nearbyCyclists.filter{ it.user.id != state.user.id }.forEach {
            val (user, bitmapProfileImage) = it
            val location = user.location
            val iconImage = if (user.userAssistance?.needHelp == true) AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_emergency)
                    ?.toBitmap(width = 120, height = 120) else bitmapProfileImage!!

            val pointAnnotationOptions =  PointAnnotationOptions()
                .withIconImage(iconImage!!)
            pointAnnotationOptions.withPoint(Point.fromLngLat(location?.longitude ?: return@forEach, location.latitude))
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    LaunchedEffect(key1 = pulsingEnabled){
        mapsMapView.location2.pulsingEnabled = pulsingEnabled
    }

    val mapStyle by remember(isDarkTheme){
        derivedStateOf {
            if (isDarkTheme) Style.DARK else Style.OUTDOORS
        }
    }

    LaunchedEffect(key1 = mapStyle){
        mapsMapboxMap.loadStyleUri(mapStyle)
    }




    AndroidViewBinding(factory = ActivityMappingBinding::inflate, modifier = modifier){
        val parentContext = this.root.context
        lateinit var mapboxMap: MapboxMap
        lateinit var mapboxNavigation: MapboxNavigation
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
            mapboxNavigation.setRoutes(listOf())

            soundButton.visibility = View.INVISIBLE
            maneuverView.visibility = View.INVISIBLE
            routeOverview.visibility = View.INVISIBLE
            tripProgressCard.visibility = View.INVISIBLE
        }
        val setRouteAndStartNavigation = { routes: List<DirectionsRoute> ->
            mapboxNavigation.setRoutes(routes)



            soundButton.visibility = View.VISIBLE
            routeOverview.visibility = View.VISIBLE
            tripProgressCard.visibility = View.VISIBLE

            navigationCamera.requestNavigationCameraToOverview()
        }

        val findRoute = { destination: Point ->
            val originLocation = navigationLocationProvider.lastLocation
            val originPoint = originLocation?.let { location ->
                Point.fromLngLat(location.longitude, location.latitude)
            }

            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .applyDefaultNavigationOptions()
                    .applyLanguageAndVoiceUnitOptions(parentContext)
                    .coordinatesList(listOf(originPoint, destination))

                    .bearingsList(
                        listOf(
                            originLocation?.bearing?.toDouble()?.let {
                                Bearing.builder()
                                    .angle(it)
                                    .degrees(45.0)
                                    .build()
                            },
                            null
                        )
                    )
                    .layersList(listOf(mapboxNavigation.getZLevel(), null))
                    .build(),
                object : RouterCallback {
                    override fun onRoutesReady(
                        routes: List<DirectionsRoute>,
                        routerOrigin: RouterOrigin
                    ) {
                        setRouteAndStartNavigation(routes)
                    }

                    override fun onFailure(
                        reasons: List<RouterFailure>,
                        routeOptions: RouteOptions
                    ) {
                        Timber.e("Route request failed")
                    }

                    override fun onCanceled(
                        routeOptions: RouteOptions,
                        routerOrigin: RouterOrigin) {

                        Timber.e("Route request canceled")
                    }
                }
            )
        }




        val locationObserver = object : LocationObserver {
            var firstLocationUpdateReceived = false

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


                if (!firstLocationUpdateReceived) {
                    firstLocationUpdateReceived = true
                    navigationCamera.requestNavigationCameraToOverview(
                        stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                            .maxDuration(0) // instant transition
                            .build()
                    )
                }
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
                    Toast.makeText(
                        parentContext,
                        error.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    maneuverView.visibility = View.VISIBLE
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

                        mapboxMap = mapView.getMapboxMap().apply {
                            setBounds(
                                CameraBoundsOptions.Builder()
                                    .minZoom(MappingConstants.MIN_ZOOM_LEVEL_MAPS)
                                    .maxZoom(MappingConstants.MAX_ZOOM_LEVEL_MAPS)
                                    .build())
                        }
                        mapView.apply {
                            scalebar.enabled = false
                            logo.enabled = false
                            attribution.enabled = false

                            location2.apply {

                                locationPuck = LocationPuck2D(
                                    bearingImage = ContextCompat.getDrawable(
                                        parentContext,
                                        com.mapbox.navigation.R.drawable.mapbox_navigation_puck_icon
                                    ),
                                    topImage = ContextCompat.getDrawable(
                                        parentContext,
                                        com.mapbox.maps.R.drawable.mapbox_mylocation_icon_default
                                    ),
                                    shadowImage = ContextCompat.getDrawable(
                                        parentContext,
                                        com.mapbox.maps.R.drawable.mapbox_user_icon_shadow
                                    )
                                )

                                showAccuracyRing = true
                                pulsingColor = ContextCompat.getColor(parentContext, R.color.ThemeColor)
                                puckBearingEnabled = false
                                pulsingMaxRadius = 120.0f
                                puckBearingSource = PuckBearingSource.HEADING
                                setLocationProvider(navigationLocationProvider)
                                enabled = true
                            }
                        }
                        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
                            MapboxNavigationProvider.retrieve()
                        } else {
                            MapboxNavigationProvider.create(
                                NavigationOptions.Builder(parentContext.applicationContext)
                                    .accessToken(parentContext.getString(R.string.MapsDownloadToken))
                                    .build()
                            )
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
                        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
                            when (navigationCameraState) {
                                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                                NavigationCameraState.FOLLOWING -> recenter.visibility = View.INVISIBLE
                                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                                NavigationCameraState.OVERVIEW,
                                NavigationCameraState.IDLE -> recenter.visibility = View.VISIBLE
                            }
                        }
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
                            MapboxDistanceFormatter(distanceFormatterOptions)
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
                            .withRouteLineBelowLayerId("road-label")
                            .build()
                        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
                        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

                        val routeArrowOptions = RouteArrowOptions.Builder(parentContext).build()
                        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

                        mapboxMap.loadStyleUri(
                            Style.MAPBOX_STREETS
                        ) {
                            mapView.gestures.addOnMapLongClickListener { point ->
//                                findRoute(point)
                                mapView.getMapboxMap().flyTo(
                                cameraOptions {
                                    center(point)
                                    zoom(LOCATE_USER_ZOOM_LEVEL)
                                    bearing(0.0)
                                },
                                MapAnimationOptions.mapAnimationOptions {
                                    duration(MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION)
                                }
                            )
                                true
                            }
                        }

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

                        mapboxNavigation.startTripSession()
                        onInitializeMapView(mapView)
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
                    }
                    Lifecycle.Event.ON_STOP -> {
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


