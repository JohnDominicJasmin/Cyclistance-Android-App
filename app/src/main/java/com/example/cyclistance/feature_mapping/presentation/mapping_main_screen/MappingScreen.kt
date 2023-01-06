package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.changeToNormalPuckIcon
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.openNavigationApp
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.startLocationServiceIntentAction
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
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
    typeBottomSheet: String = "",
    isDarkTheme: Boolean,
    mappingViewModel: MappingViewModel,
    paddingValues: PaddingValues,
    scaffoldState: ScaffoldState,
    navController: NavController) {

    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()


    var mapboxMap by remember<MutableState<MapboxMap?>> {
        mutableStateOf(null)
    }



    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )



    val onInitializeMapboxMap = remember{{ mbm: MapboxMap ->
        mapboxMap = mbm
    }}



    BackHandler(enabled = true, onBack = {
        coroutineScope.launch {

            if (scaffoldState.drawerState.isOpen) {
                scaffoldState.drawerState.close()
                return@launch
            }

            context.findActivity()?.finish()
        }
    })

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


    RequestMultiplePermissions(
        multiplePermissionsState = locationPermissionsState)

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

    val postProfile = remember {
        {
            if (!context.hasGPSConnection()) {
                context.checkLocationSetting(
                    onDisabled = settingResultRequest::launch,
                    onEnabled = {
                        mappingViewModel.onEvent(
                            event = MappingEvent.RequestHelp)

                    })
            } else {
                mappingViewModel.onEvent(
                    event = MappingEvent.RequestHelp)

            }
        }
    }

    val userLocationAvailable by remember(locationPermissionsState.allPermissionsGranted, state.userLocation) {
        derivedStateOf {
            locationPermissionsState.allPermissionsGranted.and(state.userLocation != null)
        }
    }

    val locationComponentOptions = MappingUtils.rememberLocationComponentOptions()

    val pulsingEnabled by remember(state.isSearchingForAssistance, locationPermissionsState.allPermissionsGranted) {
        derivedStateOf { state.isSearchingForAssistance.and(locationPermissionsState.allPermissionsGranted) }
    }

    val onClickRequestHelpButton = remember {{
        locationPermissionsState.requestPermission(
            context = context,
            rationalMessage = "Location permission is not yet granted.") {
            context.startLocationServiceIntentAction()
            postProfile()
        }
    }}

    val showUserLocation = remember(mapboxMap, state.isNavigating, userLocationAvailable){{
        mapboxMap?.style?.let { style ->
            if (state.isNavigating) {

                val buildLocationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(context, style)
                        .locationComponentOptions(locationComponentOptions.build())
                        .build()
                mapboxMap?.locationComponent?.apply {
                    activateLocationComponent(buildLocationComponentActivationOptions);
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
                    activateLocationComponent(buildLocationComponentActivationOptions);
                    isLocationComponentEnabled = userLocationAvailable
                    cameraMode = CameraMode.NONE
                    renderMode = RenderMode.NORMAL
                }
            }
        }
        Unit
    }}


    val showRouteDirection = remember(state.routeDirection, mapboxMap){{

        state.routeDirection?.geometry?.let { geometry ->

            mapboxMap?.getStyle { style ->
                if (style.isFullyLoaded.not() || geometry.isEmpty()) {
                    return@getStyle
                }

                val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                routeLineSource?.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                    LineString.fromPolyline(geometry, PRECISION_6))))
            }
        }
        Unit
    }}

    val removeRouteDirection = remember(mapboxMap) {{
        mapboxMap?.getStyle { style ->

            if(style.isFullyLoaded.not()){
                return@getStyle
            }

            val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
            routeLineSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
        }
        Unit
    }}





    val locateUser =
        remember(userLocationAvailable, mapboxMap) {
            { zoomLevel: Double, latLng: LatLng, cameraAnimationDuration: Int  ->

                val mapboxLoaded = mapboxMap?.locationComponent != null && mapboxMap?.style != null
                if (userLocationAvailable && mapboxLoaded) {
                    showUserLocation()
                    mapboxMap?.animateCameraPosition(
                        latLng = latLng,
                        zoomLevel = zoomLevel,
                        cameraAnimationDuration = cameraAnimationDuration)
                }
            }
        }

    val onClickLocateUserButton = remember {{
        locationPermissionsState.requestPermission(
            context = context,
            rationalMessage = "Location permission is not yet granted.",
            onGranted = {
                if (!context.hasGPSConnection()) {
                    context.checkLocationSetting(
                        onDisabled = settingResultRequest::launch)
                }

                state.userLocation?.let{
                    val point = LatLng(it.latitude, it.longitude)
                    locateUser(LOCATE_USER_ZOOM_LEVEL, point, DEFAULT_CAMERA_ANIMATION_DURATION)
                }

            })
    }}
    val openNavigationApp = remember(state.userRescueTransaction?.route){{
        val route = state.userRescueTransaction?.route
        val location = route?.destinationLocation
        location?.let{
            context.openNavigationApp(latitude = it.latitude, longitude = it.longitude)
        }
        Unit
    }}
    val onClickRouteOverViewButton = remember(mapboxMap){{
        mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING
    }}
    val onClickRecenterButton = remember(mapboxMap){{
        mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING_GPS
    }}
    val onClickOpenNavigationButton = remember{{
        openNavigationApp()
    }}

    val onClickCancelSearchButton = remember {{
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }.invokeOnCompletion {
            mappingViewModel.onEvent(event = MappingEvent.CancelRequestHelp)
            mappingViewModel.onEvent(event = MappingEvent.StopPinging)
        }
        Unit
    }}

    val onChangeCameraState = remember{{ cameraPosition: LatLng, zoom: Double ->
        mappingViewModel.onEvent(event = MappingEvent.ChangeCameraState(cameraPosition, zoom))
    }}

    val onClickCancelRescueButton = remember(state.rescuer, state.userRescueTransaction) {{
        val role = state.user.transaction?.role
        val isRescuee = role == Role.RESCUEE.name.lowercase()
        val transactionId = state.userRescueTransaction?.id
        val selectionType = if (isRescuee) SELECTION_RESCUEE_TYPE else SELECTION_RESCUER_TYPE
        val clientId = state.rescuer?.id ?: state.rescuee?.id

        navController.navigateScreen(destination = "${Screens.CancellationScreen.route}/$selectionType/$transactionId/$clientId")

    }}

    val onDismissNoInternetDialog = remember{{ ->
        mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetDialog)
    }}

    val hasTransaction = remember(key1 = state.userRescueTransaction, key2 = state.user.transaction) {
        val transaction = state.userRescueTransaction
        val rescueTransactionId = state.userRescueTransaction?.id ?: ""
        val userTransactionId = state.user.transaction?.transactionId ?: ""
        transaction != null  && rescueTransactionId.isNotEmpty() && userTransactionId.isNotEmpty()
    }

    val isRescueCancelled = remember(state.userRescueTransaction?.cancellation?.rescueCancelled, state.userRescueTransaction) {
            (state.userRescueTransaction?.cancellation)?.rescueCancelled == true
    }

    val clientPhoneNumber = remember(state.rescuee, state.rescuer) {
        val client = state.rescuee ?: state.rescuer
        client?.contactNumber
    }

    val callClient = remember(clientPhoneNumber){{
        val intent = Intent(Intent.ACTION_CALL)

        intent.data = Uri.parse("tel:$clientPhoneNumber")
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }}
    val callPhonePermissionState = rememberPermissionState(permission = Manifest.permission.CALL_PHONE){ permissionGranted ->
        if(permissionGranted){
            callClient()
        }
    }
    val onClickChatButton = remember(clientPhoneNumber){{
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("smsto:$clientPhoneNumber")
        context.startActivity(intent)
    }}

    val onClickCallButton = remember(clientPhoneNumber) {{
            callPhonePermissionState.requestPermission(
                context = context,
                rationalMessage = "Phone call permission is not yet granted.") {
                callClient()
            }
    }}

    val onRequestNavigationCameraToOverview = remember(mapboxMap){{
        val locationComponent = mapboxMap?.locationComponent
        locationComponent?.cameraMode = CameraMode.TRACKING
    }}

    val onClickOkCancelledRescue = remember{{
        mappingViewModel.onEvent(event = MappingEvent.CancelRescueTransaction)
    }}

    val onClickRescueeMapIcon = remember{{ id: String ->
        mappingViewModel.onEvent(event = MappingEvent.SelectRescueMapIcon(id))
    }}

    val onDismissRescueeBanner = remember{{
        mappingViewModel.onEvent(event = MappingEvent.DismissRescueeBanner)
    }}
    val onClickRespondToHelpButton = remember{{
        mappingViewModel.onEvent(event = MappingEvent.RespondToHelp)
    }}
    val onClickOkAcceptedRescue = remember{{
        mappingViewModel.onEvent(event = MappingEvent.StartNavigation)
        mappingViewModel.onEvent(event = MappingEvent.DismissRequestAccepted)
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(BottomSheetType.OnGoingRescue.type))
    }}


    LaunchedEffects(
        state = state,
        hasTransaction = hasTransaction,
        isRescueCancelled = isRescueCancelled,
        hasInternetConnection = hasInternetConnection,
        mapboxMap = mapboxMap,
        userLocationAvailable = userLocationAvailable,
        pulsingEnabled = pulsingEnabled,
        mappingViewModel = mappingViewModel,
        showUserLocation = showUserLocation,
        typeBottomSheet = typeBottomSheet,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        locationPermissionsState = locationPermissionsState,
        onCheckLocationSetting = {
            context.checkLocationSetting(onDisabled = settingResultRequest::launch)
        },
        navController = navController,
        locateUser = locateUser,
        onRemoveRouteDirection = removeRouteDirection,
        onShowRouteDirection = showRouteDirection,
    )


    MappingScreenContent(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        onClickRequestHelpButton = onClickRequestHelpButton,
        locationPermissionState = locationPermissionsState,
        onClickCancelSearchButton = onClickCancelSearchButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onChangeCameraState = onChangeCameraState,
        onClickCancelRescueTransactionButton = onClickCancelRescueButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        hasTransaction = hasTransaction,
        isRescueCancelled = isRescueCancelled,
        onClickCallRescueTransactionButton = onClickCallButton,
        onClickChatRescueTransactionButton = onClickChatButton,
        onClickOkButtonCancelledRescue = onClickOkCancelledRescue,
        onClickRescueeMapIcon = onClickRescueeMapIcon,
        onMapClick = onDismissRescueeBanner,
        onClickDismissBannerButton = onDismissRescueeBanner,
        onClickRespondToHelpButton = onClickRespondToHelpButton,
        onClickLocateUserButton = onClickLocateUserButton,
        onClickRouteOverButton = onClickRouteOverViewButton,
        onClickRecenterButton = onClickRecenterButton,
        onClickOpenNavigationButton = onClickOpenNavigationButton,
        onClickOkButtonRescueRequestAccepted = onClickOkAcceptedRescue,
        onRequestNavigationCameraToOverview = onRequestNavigationCameraToOverview,
        onInitializeMapboxMap = onInitializeMapboxMap,
        mapboxMap = mapboxMap,
        )

}



@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun LaunchedEffects(
    state: MappingState,
    hasTransaction: Boolean,
    isRescueCancelled: Boolean,
    hasInternetConnection: Boolean,
    mapboxMap: MapboxMap?,
    userLocationAvailable: Boolean,
    pulsingEnabled: Boolean,
    mappingViewModel: MappingViewModel,
    showUserLocation: () -> Unit,
    typeBottomSheet: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    locationPermissionsState: MultiplePermissionsState,
    onCheckLocationSetting: () -> Unit,
    onRemoveRouteDirection: () -> Unit,
    onShowRouteDirection: () -> Unit,
    navController: NavController,
    locateUser: (zoomLevel: Double, latLng: LatLng, cameraAnimationDuration: Int) -> Unit,
    ) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.routeDirection, key2 = mapboxMap){

        val route = state.routeDirection ?: return@LaunchedEffect

        if(route.geometry.isEmpty()){
            onRemoveRouteDirection()
            return@LaunchedEffect
        }
        onShowRouteDirection()
    }


    LaunchedEffect(key1 = state.userRescueTransaction?.route, key2 = hasTransaction, key3 = isRescueCancelled){
        val transactionRoute = state.userRescueTransaction?.route
        val startingLocation = transactionRoute?.startingLocation
        val destinationLocation = transactionRoute?.destinationLocation


        if(hasTransaction.not() || isRescueCancelled){
            mappingViewModel.onEvent(event = MappingEvent.RemoveRouteDirections)
            return@LaunchedEffect
        }

        startingLocation?:return@LaunchedEffect
        destinationLocation?:return@LaunchedEffect

        mappingViewModel.onEvent(
            event = MappingEvent.ShowRouteDirections(
                origin = Point.fromLngLat(startingLocation.longitude, startingLocation.latitude),
                destination = Point.fromLngLat(destinationLocation.longitude, destinationLocation.latitude)))

    }



    LaunchedEffect(key1 = hasInternetConnection) {
        val nearbyCyclistLoaded = state.nearbyCyclists != null
        val userLoaded =  state.user.id != null
        val dataHaveBeenLoaded = userLoaded && nearbyCyclistLoaded

        if(hasInternetConnection.not()){
            return@LaunchedEffect
        }

        if(dataHaveBeenLoaded.not()){
            mappingViewModel.onEvent(MappingEvent.LoadData)
        }
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
    }





    LaunchedEffect(key1 = mapboxMap, key2 = userLocationAvailable, key3= pulsingEnabled) {
        showUserLocation()
    }
    LaunchedEffect(key1 = state.isNavigating){
        showUserLocation()
    }

    LaunchedEffect(key1 = state.bottomSheetType) {
        coroutineScope.launch {
            if (state.bottomSheetType.isNotEmpty()) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }


    LaunchedEffect(key1 = typeBottomSheet) {

        if (typeBottomSheet == BottomSheetType.SearchAssistance.type) {
            mappingViewModel.onEvent(event = MappingEvent.StartPinging)
        }

        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(typeBottomSheet))


    }
    LaunchedEffect(key1 = hasTransaction, key2 = isRescueCancelled){

        if(hasTransaction.not()){
            return@LaunchedEffect
        }

        if(isRescueCancelled){
            return@LaunchedEffect
        }

        mappingViewModel.onEvent(event = MappingEvent.StopNavigation)

    }

    LaunchedEffect(key1 = userLocationAvailable, key2 = mapboxMap) {

        if(userLocationAvailable.not()){
            return@LaunchedEffect
        }

        with(state.cameraState) {
            locateUser(cameraZoom, cameraPosition, FAST_CAMERA_ANIMATION_DURATION)
        }
    }

    LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted) {
        if (!locationPermissionsState.allPermissionsGranted) {
            return@LaunchedEffect
        }

        if (!context.hasGPSConnection()) {
            onCheckLocationSetting()
        }

        context.startLocationServiceIntentAction()

    }



    LaunchedEffect(key1 = true) {

        mappingViewModel.eventFlow.collectLatest{ event ->
            when (event) {
                is MappingUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.ShowConfirmDetailsScreen -> {
                    navController.navigateScreen(
                        Screens.ConfirmDetailsScreen.route)
                }

                is MappingUiEvent.ShowEditProfileScreen -> {
                    navController.navigateScreen(
                        Screens.EditProfileScreen.route)
                }

                is MappingUiEvent.ShowSignInScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.SignInScreen.route,
                        Screens.MappingScreen.route)
                }

                else -> {}
            }
        }
    }

}
