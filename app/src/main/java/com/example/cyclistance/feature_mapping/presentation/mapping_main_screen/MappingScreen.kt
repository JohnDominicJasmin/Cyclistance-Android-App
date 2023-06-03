package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.connection.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.contexts.callPhoneNumber
import com.example.cyclistance.core.utils.contexts.startLocationServiceIntentAction
import com.example.cyclistance.core.utils.permissions.requestPermission
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.changeToNormalPuckIcon
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.openNavigationApp
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    hasInternetConnection: Boolean,
    typeBottomSheet: String,
    isDarkTheme: Boolean,
    mappingViewModel: MappingViewModel,
    paddingValues: PaddingValues,
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit,
    navController: NavController) {

    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    var uiState by rememberSaveable { mutableStateOf(MappingUiState()) }
    var cameraState by rememberSaveable{ mutableStateOf(CameraState()) }
    val locationComponentOptions = MappingUtils.rememberLocationComponentOptions()
    var mapboxMap by remember<MutableState<MapboxMap?>> {
        mutableStateOf(null)
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val locationPermissionsState = if (Build.VERSION.SDK_INT >= Q) {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    } else {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
    }


    val userLocationAvailable by remember(
        locationPermissionsState.allPermissionsGranted,
        state.userLocation) {
        derivedStateOf {
            locationPermissionsState.allPermissionsGranted.and(state.userLocation != null)
        }
    }


    val pulsingEnabled by remember(
        uiState.searchingAssistance,
        locationPermissionsState.allPermissionsGranted
    ) {
        derivedStateOf { uiState.searchingAssistance.and(locationPermissionsState.allPermissionsGranted) }
    }



    val showUserLocation = remember(mapboxMap, isNavigating, userLocationAvailable) {
        {
            mapboxMap?.style?.let { style ->
                if (isNavigating) {

                    val buildLocationComponentActivationOptions =
                        LocationComponentActivationOptions.builder(context, style)
                            .locationComponentOptions(locationComponentOptions.build())
                            .build()
                    mapboxMap?.locationComponent?.apply {
                        activateLocationComponent(buildLocationComponentActivationOptions)
                        isLocationComponentEnabled = userLocationAvailable
                        cameraMode = CameraMode.NONE
                        renderMode = RenderMode.GPS

                    }

                } else {
                    val buildLocationComponentActivationOptions =
                        LocationComponentActivationOptions.builder(context, style)
                            .locationComponentOptions(
                                locationComponentOptions
                                    .changeToNormalPuckIcon(context)
                                    .pulseEnabled(pulsingEnabled)
                                    .build())
                            .build()
                    mapboxMap?.locationComponent?.apply {
                        activateLocationComponent(buildLocationComponentActivationOptions)
                        isLocationComponentEnabled = userLocationAvailable
                        cameraMode = CameraMode.NONE
                        renderMode = RenderMode.NORMAL
                    }
                }
            }
            Unit
        }
    }


    val locateUser =
        remember(userLocationAvailable, mapboxMap) {
            { zoomLevel: Double, latLng: LatLng, cameraAnimationDuration: Int ->

                val mapboxLoaded =
                    (mapboxMap?.locationComponent != null) && (mapboxMap?.style?.isFullyLoaded
                                                               ?: false)
                if (userLocationAvailable && mapboxLoaded) {
                    showUserLocation()
                    mapboxMap?.animateCameraPosition(
                        latLng = latLng,
                        zoomLevel = zoomLevel,
                        cameraAnimationDuration = cameraAnimationDuration)
                }
            }
        }


    val onInitializeMapboxMap = remember(userLocationAvailable) {
        { mbm: MapboxMap ->
            if(mapboxMap == null){
                mapboxMap = mbm
            }

            if (userLocationAvailable) {
                val camera = cameraState
                locateUser(camera.zoom, camera.position, FAST_CAMERA_ANIMATION_DURATION)

            }
        }
    }


    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            context.startLocationServiceIntentAction()
            Timber.d("GPS Setting Request Accepted")
            return@rememberLauncherForActivityResult
        }
        Timber.d("GPS Setting Request Denied")
    }

    val requestHelp = remember {
        {
            if (!context.hasGPSConnection()) {
                context.checkLocationSetting(
                    onDisabled = settingResultRequest::launch,
                    onEnabled = {
                        mappingViewModel.onEvent(
                            event = MappingVmEvent.RequestHelp)

                    })
            } else {
                mappingViewModel.onEvent(
                    event = MappingVmEvent.RequestHelp)

            }
        }
    }

    val onClickRequestHelpButton = remember {
        {
            locationPermissionsState.requestPermission(
                context = context,
                rationalMessage = "Location permission is not yet granted.") {
                context.startLocationServiceIntentAction()
                requestHelp()
            }
        }
    }



    val showRouteDirection = remember(uiState.routeDirection, mapboxMap) {
        {

            uiState.routeDirection?.geometry?.let { geometry ->

                mapboxMap?.getStyle { style ->
                    if (style.isFullyLoaded.not() || geometry.isEmpty()) {
                        return@getStyle
                    }

                    val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    routeLineSource?.setGeoJson(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(
                                LineString.fromPolyline(geometry, PRECISION_6))))
                }
            }
            Unit
        }
    }

    val removeRouteDirection = remember(mapboxMap) {
        {
            mapboxMap?.getStyle { style ->

                if (style.isFullyLoaded.not()) {
                    return@getStyle
                }

                val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                routeLineSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
            }
            Unit
        }
    }




    val onClickLocateUserButton = remember {
        {
            locationPermissionsState.requestPermission(
                context = context,
                rationalMessage = "Location permission is not yet granted.",
                onGranted = {
                    if (!context.hasGPSConnection()) {
                        context.checkLocationSetting(
                            onDisabled = settingResultRequest::launch)
                    }
                    state.userLocation?.let {
                        it.latitude ?: return@let
                        it.longitude ?: return@let
                        val point = LatLng(it.latitude, it.longitude)
                        locateUser(LOCATE_USER_ZOOM_LEVEL, point, DEFAULT_CAMERA_ANIMATION_DURATION)
                    }

                })
        }
    }

    val openNavigationApp = remember(state.rescueTransaction?.route) {
        {
            val route = state.rescueTransaction?.route
            val location = route?.destinationLocation
            location?.let {
                it.latitude ?: return@let
                it.longitude ?: return@let
                context.openNavigationApp(latitude = it.latitude, longitude = it.longitude)
            }
            Unit
        }
    }

    val onClickRouteOverViewButton = remember(mapboxMap) {
        {
            mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING
        }
    }

    val onClickRecenterButton = remember(mapboxMap) {
        {
            mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING_GPS
        }
    }

    val onClickOpenNavigationButton = remember {
        {
            openNavigationApp()
        }
    }

    val onClickCancelSearchButton = remember {
        {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }.invokeOnCompletion {
                mappingViewModel.onEvent(event = MappingVmEvent.CancelRequestHelp)
                uiState = uiState.copy(searchingAssistance = false)
            }
            Unit
        }
    }

    val onChangeCameraPosition = remember {
        { _cameraState: CameraState ->
            cameraState = _cameraState

        }
    }

    val onClickCancelRescueButton = remember(state.rescuer, state.rescueTransaction) {
        {
            val role = state.user.transaction?.role
            val isRescuee = role == Role.RESCUEE.name.lowercase()
            val transactionId = state.rescueTransaction?.id
            val selectionType = if (isRescuee) SELECTION_RESCUEE_TYPE else SELECTION_RESCUER_TYPE
            val clientId = state.rescuer?.id ?: state.rescuee?.id

            navController.navigateScreen(destination = "${Screens.CancellationScreen.route}/$selectionType/$transactionId/$clientId")

        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetVisible = false
            )
        }
    }

    val hasTransaction = remember(key1 = state.rescueTransaction, key2 = state.user.transaction) {
        val transaction = state.rescueTransaction
        val rescueTransactionId = state.rescueTransaction?.id ?: ""
        val userTransactionId = state.user.transaction?.transactionId ?: ""
        transaction != null && rescueTransactionId.isNotEmpty() && userTransactionId.isNotEmpty()
    }

    val isRescueCancelled =
        remember(state.rescueTransaction?.cancellation?.rescueCancelled, state.rescueTransaction) {
            (state.rescueTransaction?.cancellation)?.rescueCancelled == true
        }

    val clientPhoneNumber = remember(state.rescuee, state.rescuer) {
        val client = state.rescuee ?: state.rescuer
        client?.contactNumber
    }

    val callClient = remember(clientPhoneNumber) {
        {
            clientPhoneNumber?.let(context::callPhoneNumber)
        }
    }
    val phonePermissionState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE) { permissionGranted ->
            if (permissionGranted) {
                callClient()
            }
        }
    val onClickChatButton = remember(clientPhoneNumber) {
        {


        }
    }

    val onClickCallButton = remember(clientPhoneNumber) {
        {
            callPhonePermissionState.requestPermission(
                context = context,
                rationalMessage = "Phone call permission is not yet granted.") {
                callClient()
            }
        }
    }

    val onRequestNavigationCameraToOverview = remember(mapboxMap) {
        {
            val locationComponent = mapboxMap?.locationComponent
            locationComponent?.cameraMode = CameraMode.TRACKING
        }
    }

    val onClickOkCancelledRescue = remember {
        {
            mappingViewModel.onEvent(event = MappingVmEvent.CancelRescueTransaction)
        }
    }

    val onClickRescueeMapIcon = remember {
        { id: String ->
            mappingViewModel.onEvent(event = MappingVmEvent.SelectRescueMapIcon(id))
        }
    }

    val onDismissRescueeBanner = remember{{
        val isRescueeBannerVisible = uiState.mapSelectedRescuee != null
        if (isRescueeBannerVisible) {
            uiState = uiState.copy(
                mapSelectedRescuee = null,
                requestHelpButtonVisible = true
            )
        }
    }
    }

    val onMapClick = remember {{
        onDismissRescueeBanner()
    }}

    val onClickRespondToHelpButton = remember {
        {
            uiState.mapSelectedRescuee?.let {
                mappingViewModel.onEvent(
                    event = MappingVmEvent.RespondToHelp(
                        selectedRescuee = it
                    ))
            }
            Unit
        }
    }
    val onClickOkAcceptedRescue = remember {
        {
            onChangeNavigatingState(true)
            uiState = uiState.copy(
                rescueRequestAccepted = false,
                bottomSheetType = BottomSheetType.OnGoingRescue.type
            )
        }
    }


    LaunchedEffect(key1 = true, key2 = state.userLocation) {

        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {


                is MappingEvent.RequestHelpSuccess -> {
                    navController.navigateScreen(
                        Screens.ConfirmDetailsScreen.route + "?$LATITUDE=${state.userLocation?.latitude}&$LONGITUDE=${state.userLocation?.longitude}")
                }

                is MappingEvent.InsufficientUserCredential -> {
                    navController.navigateScreen(
                        Screens.EditProfileScreen.route)
                }

                is MappingEvent.LocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RescuerLocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.UserFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RespondToHelpSuccess -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.AddressFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetVisible = true
                    )
                }

                is MappingEvent.NewSelectedRescuee -> {
                    uiState = uiState.copy(
                        mapSelectedRescuee = event.selectedRescuee,
                        requestHelpButtonVisible = false
                    )
                }

                is MappingEvent.NewRouteDirection -> {
                    uiState = uiState.copy(
                        routeDirection = event.routeDirection
                    )
                }

                is MappingEvent.RemoveAssignedTransactionSuccess -> {
                    uiState = uiState.copy(
                        rescueRequestAccepted = false,
                        requestHelpButtonVisible = true,
                        searchingAssistance = false,
                        bottomSheetType = ""
                    )
                    onChangeNavigatingState(false)

                }

                is MappingEvent.RescueRequestAccepted -> {
                     uiState = uiState.copy(
                         rescueRequestAccepted = true
                     )
                }

                is MappingEvent.CancelHelpRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = true
                    )
                }

                is MappingEvent.AcceptRescueRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = false
                    )
                }

                is MappingEvent.FailedToCalculateDistance -> {
                    Toast.makeText(context, "Failed to Calculate Distance", Toast.LENGTH_SHORT)
                        .show()
                }

                is MappingEvent.DestinationReached -> {
                    val role = state.user.transaction?.role
                    val type = if (role == Role.RESCUEE.name.lowercase()) {
                        BottomSheetType.RescuerArrived.type
                    } else {
                        BottomSheetType.DestinationReached.type
                    }
                    uiState = uiState.copy(bottomSheetType = type)

                }

                is MappingEvent.RemoveRespondentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }






    LaunchedEffect(key1 = uiState.routeDirection, key2 = mapboxMap) {

        val route = uiState.routeDirection ?: return@LaunchedEffect

        if (route.geometry.isEmpty()) {
            removeRouteDirection()
            return@LaunchedEffect
        }
        showRouteDirection()
    }


    LaunchedEffect(
        key1 = state.rescueTransaction?.route,
        key2 = hasTransaction,
        key3 = isRescueCancelled) {

        val transactionRoute = state.rescueTransaction?.route
        val startingLocation = transactionRoute?.startingLocation
        val destinationLocation = transactionRoute?.destinationLocation


        if (hasTransaction.not() || isRescueCancelled) {
            uiState = uiState.copy(routeDirection = null)
            return@LaunchedEffect
        }

        startingLocation?.longitude ?: return@LaunchedEffect
        startingLocation.latitude ?: return@LaunchedEffect
        destinationLocation?.longitude ?: return@LaunchedEffect
        destinationLocation.latitude ?: return@LaunchedEffect

        mappingViewModel.onEvent(
            event = MappingVmEvent.GetRouteDirections(
                origin = Point.fromLngLat(startingLocation.longitude, startingLocation.latitude),
                destination = Point.fromLngLat(
                    destinationLocation.longitude,
                    destinationLocation.latitude)))

    }



    LaunchedEffect(key1 = hasInternetConnection) {
        val nearbyCyclistLoaded = state.nearbyCyclists != null
        val userLoaded = state.user.id != null
        val dataHaveBeenLoaded = userLoaded && nearbyCyclistLoaded

        if (hasInternetConnection.not()) {
            return@LaunchedEffect
        }

        if (dataHaveBeenLoaded.not()) {
            mappingViewModel.onEvent(MappingVmEvent.LoadData)
        }
        mappingViewModel.onEvent(MappingVmEvent.SubscribeToDataChanges)
    }



    LaunchedEffect(key1 = isNavigating, key2 = userLocationAvailable, key3 = pulsingEnabled) {
        showUserLocation()
    }

    LaunchedEffect(key1 = uiState.bottomSheetType) {
        coroutineScope.launch {
            if (uiState.bottomSheetType.isNotEmpty()) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }


    LaunchedEffect(key1 = typeBottomSheet) {

        if (typeBottomSheet == BottomSheetType.SearchAssistance.type) {
            uiState = uiState.copy(searchingAssistance = true)
        }
        uiState = uiState.copy(bottomSheetType = typeBottomSheet)


    }
    LaunchedEffect(key1 = hasTransaction, key2 = isRescueCancelled) {

        if (hasTransaction.not()) {
            return@LaunchedEffect
        }

        if (isRescueCancelled) {
            return@LaunchedEffect
        }

        onChangeNavigatingState(false)

    }


    LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted) {
        if (!locationPermissionsState.allPermissionsGranted) {
            return@LaunchedEffect
        }

        if (!context.hasGPSConnection()) {
            context.checkLocationSetting(onDisabled = settingResultRequest::launch)
        }

        context.startLocationServiceIntentAction()

    }





    MappingScreenContent(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        locationPermissionState = locationPermissionsState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        hasTransaction = hasTransaction,
        isRescueCancelled = isRescueCancelled,
        mapboxMap = mapboxMap,
        isNavigating = isNavigating,
        uiState = uiState,
        event = { event ->
            when(event){
                is MappingUiEvent.RequestHelp -> onClickRequestHelpButton()
                is MappingUiEvent.RespondToHelp -> onClickRespondToHelpButton()
                is MappingUiEvent.CancelSearchConfirmed -> onClickCancelSearchButton()
                is MappingUiEvent.CallRescueTransaction -> onClickCallButton()
                is MappingUiEvent.ChatRescueTransaction -> onClickChatButton()
                is MappingUiEvent.CancelRescueTransaction -> onClickCancelRescueButton()
                is MappingUiEvent.CancelledRescueConfirmed -> onClickOkCancelledRescue()
                is MappingUiEvent.OnInitializeMap -> onInitializeMapboxMap(event.mapboxMap)
                is MappingUiEvent.RescueRequestAccepted -> onClickOkAcceptedRescue()
                is MappingUiEvent.OnChangeCameraState -> onChangeCameraPosition(event.cameraState)
                is MappingUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is MappingUiEvent.RescueeMapIconSelected -> onClickRescueeMapIcon(event.id)
                is MappingUiEvent.OnMapClick -> onMapClick()
                is MappingUiEvent.DismissBanner -> onDismissRescueeBanner()
                is MappingUiEvent.LocateUser -> onClickLocateUserButton()
                is MappingUiEvent.RouteOverview -> onClickRouteOverViewButton()
                is MappingUiEvent.RecenterRoute -> onClickRecenterButton()
                is MappingUiEvent.OpenNavigation -> onClickOpenNavigationButton()
                is MappingUiEvent.OnRequestNavigationCameraToOverview -> onRequestNavigationCameraToOverview()
                is MappingUiEvent.RescueArrivedConfirmed -> {}
                is MappingUiEvent.DestinationReachedConfirmed -> {}
            }
        }



    )

}


