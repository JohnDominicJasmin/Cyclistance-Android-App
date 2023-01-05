package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

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
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.feature_mapping_screen.domain.model.Role
import com.example.cyclistance.feature_mapping_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.CancelledRescueModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.changeToNormalPuckIcon
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.openNavigationApp
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils.startLocationServiceIntentAction
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
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

    var mapView by remember {
        mutableStateOf(
            MapView(context))
    }
    var mapboxMap by remember<MutableState<MapboxMap?>> {
        mutableStateOf(null)
    }



    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val onInitializeMapView = remember{{ mv: MapView ->
        mapView = mv
    }}

    val onInitializeMapboxMap = remember{{ mbm: MapboxMap ->
        mapboxMap = mbm
    }}




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
            (state.userRescueTransaction?.cancellation)?.rescueCancelled ?: false
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


    LaunchedEffect(key1 = mapboxMap, key2 = userLocationAvailable, pulsingEnabled) {
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
            context.checkLocationSetting(onDisabled = settingResultRequest::launch)
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





    MappingScreen(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        onClickSearchButton = onClickSearchButton,
        locationPermissionState = locationPermissionsState,
        mapView = mapView,
        onClickCancelSearchButton = onClickCancelSearchButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onInitializeMapView = onInitializeMapView,
        onChangeCameraState = onChangeCameraState,
        onClickCancelRescueTransactionButton = onClickCancelRescueButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        hasTransaction = hasTransaction,
        userLocationAvailable = userLocationAvailable,
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



@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun MappingScreen(
    modifier: Modifier,
    isDarkTheme: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    mapView: MapView,
    mapboxMap: MapboxMap?,
    hasTransaction: Boolean = false,
    isRescueCancelled: Boolean = false,
    userLocationAvailable: Boolean = false,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickSearchButton: () -> Unit = {},
    onClickRespondToHelpButton: () -> Unit = {},
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    onClickOkButtonCancelledRescue: () -> Unit = {},
    onInitializeMapboxMap: (MapboxMap) -> Unit = {},
    onClickOkButtonRescueRequestAccepted: () -> Unit = {},
    onInitializeMapView: (MapView) -> Unit = {},
    onChangeCameraState: (LatLng, Double) -> Unit = { _, _ -> },
    onDismissNoInternetDialog: () -> Unit = {},
    onClickRescueeMapIcon: (String) -> Unit = {},
    onMapClick: () -> Unit = {},
    onClickDismissBannerButton : () -> Unit = {},
    onClickLocateUserButton: () -> Unit = {},
    onClickRouteOverButton: () -> Unit = {},
    onClickRecenterButton: () -> Unit = {},
    onClickOpenNavigationButton: () -> Unit = {},
    onRequestNavigationCameraToOverview: () -> Unit = {},
) {

    val configuration = LocalConfiguration.current





    MappingBottomSheet(
        isDarkTheme = isDarkTheme,
        state = state,
        onClickRescueArrivedButton = onClickRescueArrivedButton,
        onClickReachedDestinationButton = onClickReachedDestinationButton,
        onClickCancelSearchButton = onClickCancelSearchButton,
        onClickCallRescueTransactionButton = onClickCallRescueTransactionButton,
        onClickChatRescueTransactionButton = onClickChatRescueTransactionButton,
        onClickCancelRescueTransactionButton = onClickCancelRescueTransactionButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState) {

        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()) {

            val (mapScreen, requestHelpButton, circularProgressbar, noInternetScreen, respondToHelpButton, floatingButtonSection) = createRefs()


            MappingMapsScreen(
                state = state,
                modifier = Modifier.constrainAs(mapScreen) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                isDarkTheme = isDarkTheme,
                userLocationAvailable = userLocationAvailable,
                locationPermissionsState = locationPermissionState,
                mapMapView = mapView,
                onInitializeMapView = onInitializeMapView,
                onInitializeMapboxMap = onInitializeMapboxMap,
                onChangeCameraState = onChangeCameraState,
                hasTransaction = hasTransaction,
                isRescueCancelled = isRescueCancelled,
                onClickRescueeMapIcon = onClickRescueeMapIcon,
                onMapClick = onMapClick,
                requestNavigationCameraToOverview = onRequestNavigationCameraToOverview,
                mapboxMap = mapboxMap
            )




            AnimatedVisibility(
                visible = state.selectedRescueeMapIcon != null,
                enter = expandVertically(expandFrom = Alignment.Top) { 20 },
                exit = shrinkVertically(animationSpec = tween()) { fullHeight ->
                    fullHeight / 2
                },
            ) {
                if (state.selectedRescueeMapIcon != null) {
                    MappingExpandableBanner(
                        modifier = Modifier
                            .padding(all = 6.dp)
                            .fillMaxWidth(), banner = state.selectedRescueeMapIcon,
                        onClickDismissButton = onClickDismissBannerButton)
                }
            }

            FloatingButtonSection(
                modifier = Modifier
                    .constrainAs(floatingButtonSection) {
                        end.linkTo(parent.end, margin = 4.dp)
                        bottom.linkTo(
                            parent.bottom,
                            margin = (configuration.screenHeightDp / 3).dp)
                    },
                locationPermissionGranted = locationPermissionState.allPermissionsGranted,
                state = state,
                onClickLocateUserButton = onClickLocateUserButton,
                onClickRouteOverButton = onClickRouteOverButton,
                onClickRecenterButton = onClickRecenterButton,
                onClickOpenNavigationButton = onClickOpenNavigationButton
            )

            RequestHelpButton(
                modifier = Modifier.constrainAs(requestHelpButton) {
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }, onClickSearchButton = onClickSearchButton,
                state = state
            )

            RespondToHelpButton(
                modifier = Modifier.constrainAs(respondToHelpButton) {
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
                onClickRespondButton = onClickRespondToHelpButton,
                state = state
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.constrainAs(circularProgressbar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    this.centerTo(parent)
                })
            }

            if (!state.hasInternet) {
                NoInternetDialog(
                    onDismiss = onDismissNoInternetDialog,
                    modifier = Modifier.constrainAs(noInternetScreen) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                        height = Dimension.wrapContent
                    })

            }

            AnimatedVisibility(
                visible = isRescueCancelled && state.isRescueRequestAccepted.not(),
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 220))) {

                val rescueTransaction = state.userRescueTransaction
                val cancellation = rescueTransaction?.cancellation
                val cancellationReason =
                    cancellation?.cancellationReason ?: return@AnimatedVisibility


                MappingCancelledRescue(
                    modifier = Modifier.fillMaxSize(),
                    onClickOkButton = onClickOkButtonCancelledRescue,
                    cancelledRescueModel = CancelledRescueModel(
                        transactionID = rescueTransaction.id,
                        rescueCancelledBy = cancellation.nameCancelledBy,
                        reason = cancellationReason.reason,
                        message = cancellationReason.message
                    ))
            }

            AnimatedVisibility(
                visible = state.isRescueRequestAccepted && isRescueCancelled.not(),
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 220))) {
                RescueRequestAccepted(
                    modifier = Modifier.fillMaxSize(),
                    onClickOkButton = onClickOkButtonRescueRequestAccepted,
                    acceptedName = state.rescuee?.name ?: "Name placeholder",
                )
            }







        }
    }
}





