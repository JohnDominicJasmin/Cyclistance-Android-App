package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.Manifest
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.feature_mapping_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.CancelledRescueModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.findRoute
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.rememberMapboxNavigation
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.startLocationServiceIntentAction
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.cyclistance.R as Resource


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
    var mapView by remember{ mutableStateOf(MapView(context = context, mapInitOptions = MapInitOptions(
        context,
        resourceOptions = ResourceOptions
            .Builder()
            .accessToken(context.getString(com.example.cyclistance.R.string.MapsDownloadToken)).build()))) }

    var navigationCamera by remember {
        mutableStateOf(
            NavigationCamera(
                mapboxMap = mapView.getMapboxMap(),
                viewportDataSource = MapboxNavigationViewportDataSource(mapView.getMapboxMap()),
                cameraPlugin = mapView.camera))
    }

    val mapboxNavigation = rememberMapboxNavigation(parentContext = context)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )

    val onInitializeMapView = remember{{ mv: MapView ->
        mapView = mv
    }}

    val onInitializeNavigationCamera = remember{{ nc: NavigationCamera ->
        navigationCamera = nc
    }}




    LaunchedEffect(key1 = hasInternetConnection) {
        val dataHaveBeenLoaded = state.user.id != null
        if (hasInternetConnection && dataHaveBeenLoaded.not()) {
            mappingViewModel.onEvent(MappingEvent.LoadData)
        }
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
                            event = MappingEvent.SearchAssistance)

                    })
            } else {
                mappingViewModel.onEvent(
                    event = MappingEvent.SearchAssistance)

            }
        }
    }

    val userLocationAvailable by remember(locationPermissionsState.allPermissionsGranted, state.userLocation.latitude, state.userLocation.longitude) {
        derivedStateOf {
            locationPermissionsState.allPermissionsGranted.and(state.userLocation.latitude != DEFAULT_LATITUDE && state.userLocation.longitude != DEFAULT_LONGITUDE)
        }
    }

    val onClickSearchButton = remember {{
        locationPermissionsState.requestPermission(
            context = context,
            rationalMessage = "Location permission is not yet granted.") {
            context.startLocationServiceIntentAction()
            postProfile()
        }
    }}

    val locationPuck2D = remember{
        LocationPuck2D(
            bearingImage = ContextCompat.getDrawable(
                context,
                Resource.drawable.ic_bearing_image_small),
            topImage = ContextCompat.getDrawable(
                context,
                Resource.drawable.ic_location_top_image_small),
            shadowImage = ContextCompat.getDrawable(
                context,
                com.mapbox.maps.R.drawable.mapbox_user_icon_shadow)
        )
    }

    val locateUser =
        remember(mapView) {
            { zoomLevel: Double, point: Point, cameraAnimationDuration: Long  ->
                if (userLocationAvailable) {
                    mapView.location2.apply {
                        locationPuck = locationPuck2D
                        enabled = true
                    }

                    mapView.getMapboxMap().flyTo(
                        cameraOptions {
                            center(point)
                            zoom(zoomLevel)
                            bearing(0.0)
                        },
                        mapAnimationOptions {
                            duration(cameraAnimationDuration)
                        }
                    )
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
                val point = Point.fromLngLat(state.userLocation.longitude, state.userLocation.latitude)
                locateUser(LOCATE_USER_ZOOM_LEVEL, point, DEFAULT_CAMERA_ANIMATION_DURATION)
            })
    }}

    val onClickCancelSearchButton = remember {{
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }.invokeOnCompletion {
            mappingViewModel.onEvent(event = MappingEvent.CancelSearchAssistance)
            mappingViewModel.onEvent(event = MappingEvent.StopPinging)
        }
        Unit
    }}

    val onChangeCameraState = remember{{ cameraPosition: Point, zoom: Double ->
        mappingViewModel.onEvent(event = MappingEvent.ChangeCameraState(cameraPosition, zoom))
    }}

    val onClickCancelRescueTransactionButton = remember(state.rescuer) {{
        val transactionId = state.userRescueTransaction?.id
        val selectionType = if (state.rescuer?.id != null) SELECTION_RESCUEE_TYPE else SELECTION_RESCUER_TYPE
        val clientId = state.rescuer?.id ?: state.rescuee?.id

            navController.navigateScreen(destination = "${Screens.CancellationScreen.route}/$selectionType/$transactionId/$clientId")

        }
    }

    val onDismissNoInternetDialog = remember{{ ->
        mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetDialog)
    }}

    val hasTransaction = remember(key1 = state.userRescueTransaction, key2 = state.user.transaction) {
        val transaction = state.userRescueTransaction
        val rescueTransactionId = state.userRescueTransaction?.id ?: ""
        val userTransactionId = state.user.transaction?.transactionId ?: ""
        transaction != null  && rescueTransactionId.isNotEmpty() && userTransactionId.isNotEmpty()
    }

    val isRescueCancelled = remember(state.userRescueTransaction?.cancellation?.rescueCancelled) {
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
    val onClickChatRescueTransactionButton = remember(clientPhoneNumber){{
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("smsto:$clientPhoneNumber")
        context.startActivity(intent)
    }}

    val onClickCallRescueTransactionButton = remember(clientPhoneNumber) {{
            callPhonePermissionState.requestPermission(
                context = context,
                rationalMessage = "Phone call permission is not yet granted.") {
                callClient()
            }
    }}


    val onClickOkButtonCancelledRescue = remember{{
      mappingViewModel.onEvent(event = MappingEvent.RemovedRescueTransaction)
    }}













    LaunchedEffect(key1 = typeBottomSheet) {

        if (typeBottomSheet == BottomSheetType.SearchAssistance.type) {
            mappingViewModel.onEvent(event = MappingEvent.StartPinging)
        }

        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(typeBottomSheet))


    }

    LaunchedEffect(key1 = userLocationAvailable, key2 = mapView) {

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

    LaunchedEffect(key1 = mapView, key2 = userLocationAvailable) {
        mapView.location2.apply {

            locationPuck = locationPuck2D
            enabled = true


        }
    }

    LaunchedEffect(key1 = state.userRescueTransaction?.route, key2 = hasTransaction, key3 = isRescueCancelled){
        val transactionRoute = state.userRescueTransaction?.route
        val startingLocation = transactionRoute?.startingLocation
        val destinationLocation = transactionRoute?.destinationLocation

        if (hasTransaction.not() && isRescueCancelled) {
            mapboxNavigation.setNavigationRoutes(listOf())
            return@LaunchedEffect
        }

        startingLocation?.let {
            destinationLocation?.let {
                mapboxNavigation.findRoute(context, originPoint = Point.fromLngLat(startingLocation.longitude, startingLocation.latitude),
                    destinationPoint = Point.fromLngLat(destinationLocation.longitude, destinationLocation.latitude)) {

                    mapboxNavigation.setNavigationRoutes(it)
                    navigationCamera.requestNavigationCameraToOverview()
                }
            }
        }
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
        onClickLocateUserButton = onClickLocateUserButton,
        mapView = mapView,
        onClickCancelSearchButton = onClickCancelSearchButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onInitializeMapView = onInitializeMapView,
        onChangeCameraState = onChangeCameraState,
        mapboxNavigation = mapboxNavigation,
        onInitializeNavigationCamera = onInitializeNavigationCamera,
        onClickCancelRescueTransactionButton = onClickCancelRescueTransactionButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        hasTransaction = hasTransaction,
        isRescueCancelled = isRescueCancelled,
        onClickCallRescueTransactionButton = onClickCallRescueTransactionButton,
        onClickChatRescueTransactionButton = onClickChatRescueTransactionButton,
        onClickOkButtonCancelledRescue = onClickOkButtonCancelledRescue
    )

}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun MappingScreenPreview() {
    val context = LocalContext.current
    val mapView by remember {
        mutableStateOf(
            MapView(
                context, mapInitOptions = MapInitOptions(
                    context,
                    resourceOptions = ResourceOptions
                        .Builder()
                        .accessToken(context.getString(com.example.cyclistance.R.string.MapsDownloadToken))
                        .build())))
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded))


    CyclistanceTheme(true) {

        MappingScreen(
            modifier = Modifier,
            isDarkTheme = true,
            state = MappingState(
                bottomSheetType = BottomSheetType.SearchAssistance.type,
                searchAssistanceButtonVisible = false),
            onClickSearchButton = {},
            onClickLocateUserButton = {},
            mapView = mapView,
            bottomSheetScaffoldState = bottomSheetScaffoldState,
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun MappingScreen(
    modifier: Modifier,
    isDarkTheme: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    mapView: MapView,
    mapboxNavigation: MapboxNavigation? = null,
    hasTransaction: Boolean = false,
    isRescueCancelled: Boolean = false,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickLocateUserButton: () -> Unit = {},
    onClickSearchButton: () -> Unit = {},
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    onClickOkButtonCancelledRescue: () -> Unit = {},
    onInitializeMapView: (MapView) -> Unit = {},
    onInitializeNavigationCamera: (NavigationCamera) -> Unit = {},
    onChangeCameraState: (Point, Double) -> Unit = { _, _ -> },
    onDismissNoInternetDialog: () -> Unit = {},
) {

    val configuration = LocalConfiguration.current


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()) {

        val (mapScreen, searchButton, circularProgressbar, noInternetScreen, locateButton, cancelledRescue) = createRefs()



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

            MappingMapsScreen(
                state = state,
                modifier = Modifier.constrainAs(mapScreen) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                isDarkTheme = isDarkTheme,
                locationPermissionsState = locationPermissionState,
                mapsMapView = mapView,
                onInitializeMapView = onInitializeMapView,
                onChangeCameraState = onChangeCameraState,
                mapboxNavigation = mapboxNavigation!!,
                onInitializeNavigationCamera = onInitializeNavigationCamera,
                hasTransaction = hasTransaction,
                isRescueCancelled = isRescueCancelled
            )



            LocateUserButton(
                modifier = Modifier
                    .size(53.dp)
                    .constrainAs(locateButton) {
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(parent.bottom, margin = (configuration.screenHeightDp / 3).dp)
                    },
                locationPermissionGranted = locationPermissionState.allPermissionsGranted,
                onClick = onClickLocateUserButton
            )

            RequestHelpButton(
                enabled = !state.isLoading,
                modifier = Modifier.constrainAs(searchButton) {
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.value(45.dp)
                    width = Dimension.wrapContent
                }, onClickSearchButton = onClickSearchButton,
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

            if (isRescueCancelled) {

                val rescueTransaction = state.userRescueTransaction

                val cancellation = rescueTransaction?.cancellation

                val cancellationReason = cancellation?.cancellationReason


                MappingCancelledRescue(
                    onClickOkButton = onClickOkButtonCancelledRescue,
                    cancelledRescueModel = CancelledRescueModel(
                        transactionID = rescueTransaction!!.id,
                        rescueCancelledBy = cancellation!!.nameCancelledBy,
                        reason = cancellationReason!!.reason,
                        message = cancellationReason.message
                    ))
            }

        }
    }
}







