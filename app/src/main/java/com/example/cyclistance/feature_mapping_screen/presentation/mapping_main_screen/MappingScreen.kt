package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.Manifest
import android.app.Activity.RESULT_OK
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
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.location.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.LocateUserButton
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingBottomSheet
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingMapsScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.SearchAssistanceButton
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.startLocationServiceIntentAction
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.locationcomponent.location2
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.cyclistance.R as Resource


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    typeBottomSheet: String = "",
    isDarkTheme: Boolean,
    mappingViewModel: MappingViewModel,
    paddingValues: PaddingValues,
    scaffoldState: ScaffoldState,
    navController: NavController) {

    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsState()
    val userDrawableImage by mappingViewModel.userDrawableImage
    val coroutineScope = rememberCoroutineScope()
    var mapView by remember{ mutableStateOf(MapView(context = context, mapInitOptions = MapInitOptions(
        context,
        resourceOptions = ResourceOptions
            .Builder()
            .accessToken(context.getString(com.example.cyclistance.R.string.MapsDownloadToken)).build()))) }


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )

    val onInitializeMapView = remember{{ mv: MapView ->
        mapView = mv
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
                            event = MappingEvent.SearchAssistance)

                    })
            } else {
                mappingViewModel.onEvent(
                    event = MappingEvent.SearchAssistance)

            }
        }
    }

    val userLocationAvailable by remember(locationPermissionsState.allPermissionsGranted, state.latitude, state.longitude) {
        derivedStateOf {
            locationPermissionsState.allPermissionsGranted.and(state.latitude != DEFAULT_LATITUDE && state.longitude != DEFAULT_LONGITUDE)
        }
    }
    val onClickNoInternetRetryButton = remember {
        {
            if (context.hasInternetConnection()) {
                mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetScreen)
            }
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


    val locateUser =
        remember(mapView) {
            { zoomLevel: Double ->
                if (userLocationAvailable) {
                    mapView.location2.apply {
                        enabled = true
                    }
                    val point = Point.fromLngLat(state.longitude, state.latitude)
                    mapView.getMapboxMap().flyTo(
                        cameraOptions {
                            center(point)
                            zoom(zoomLevel)
                            bearing(0.0)
                        },
                        mapAnimationOptions {
                            duration(DEFAULT_CAMERA_ANIMATION_DURATION)
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
                locateUser(LOCATE_USER_ZOOM_LEVEL)

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


    LaunchedEffect(key1 = typeBottomSheet) {
        if (typeBottomSheet.isNotEmpty()) {
            mappingViewModel.onEvent(event = MappingEvent.StartPinging)
            mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(typeBottomSheet))
        }

    }

    LaunchedEffect(key1 = userLocationAvailable) {
        locateUser(DEFAULT_MAP_ZOOM_LEVEL)
        mappingViewModel.onEvent(event = MappingEvent.PostLocation)
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

    LaunchedEffect(key1 = userDrawableImage) {
        mapView.location2.apply {


            if (userDrawableImage != null) {

                locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(
                        context,
                        Resource.drawable.ic_bearing_image_large),
                    topImage = userDrawableImage,
                    shadowImage = ContextCompat.getDrawable(
                        context,
                        com.mapbox.maps.R.drawable.mapbox_user_icon_shadow)
                )

                return@apply
            }

            locationPuck = LocationPuck2D(
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
    }

    LaunchedEffect(key1 = true) {

        with(mappingViewModel) {

            onEvent(event = MappingEvent.LoadUserImageLocationPuck)
            onEvent(event = MappingEvent.SubscribeToNearbyUsersChanges)
            onEvent(event = MappingEvent.SubscribeToLocationUpdates)
            onEvent(event = MappingEvent.SubscribeToRescueTransactionChanges)

            eventFlow.collectLatest { event ->
                when (event) {
                    is MappingUiEvent.ShowToastMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }

                    is MappingUiEvent.ShowConfirmDetailsScreen -> {
                        navController.navigateScreen(
                            Screens.ConfirmDetailsScreen.route,
                            Screens.MappingScreen.route)
                    }

                    is MappingUiEvent.ShowEditProfileScreen -> {
                        navController.navigateScreen(
                            Screens.EditProfileScreen.route,
                            Screens.MappingScreen.route)
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





    MappingScreen(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        onClickNoInternetRetryButton = onClickNoInternetRetryButton,
        onClickSearchButton = onClickSearchButton,
        locationPermissionState = locationPermissionsState,
        onClickLocateUserButton = onClickLocateUserButton,
        mapboxMap = mapView.getMapboxMap(), //todo: remove later
        mapView = mapView,
        onClickCancelSearchButton = onClickCancelSearchButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        onInitializeMapView = onInitializeMapView,

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
    val mapboxMap = remember {
        mapView.getMapboxMap()
    }

    CyclistanceTheme(true) {

        MappingScreen(
            modifier = Modifier,
            isDarkTheme = true,
            state = MappingState(bottomSheetType = BottomSheetType.SearchAssistance.type, searchAssistanceButtonVisible = false),
            onClickNoInternetRetryButton = {},
            onClickSearchButton = {},
            onClickLocateUserButton = {},
            mapView = mapView,
            mapboxMap = mapboxMap,
            bottomSheetScaffoldState = bottomSheetScaffoldState
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
    mapboxMap: MapboxMap,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickNoInternetRetryButton: () -> Unit = {},
    onClickLocateUserButton: () -> Unit = {},
    onClickSearchButton: () -> Unit = {},
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallButton: () -> Unit = {},
    onClickChatButton: () -> Unit = {},
    onClickCancelButton: () -> Unit = {},
    onInitializeMapView: (MapView) -> Unit = {},
    ) {

    val configuration = LocalConfiguration.current


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()) {

        val (mapScreen, searchButton, circularProgressbar, noInternetScreen, locateButton) = createRefs()



        MappingBottomSheet(
            isDarkTheme = isDarkTheme,
            bottomSheetType = state.bottomSheetType,
            onClickRescueArrivedButton = onClickRescueArrivedButton,
            onClickReachedDestinationButton = onClickReachedDestinationButton,
            onClickCancelSearchButton = onClickCancelSearchButton,
            onClickCallButton = onClickCallButton,
            onClickChatButton = onClickChatButton,
            onClickCancelButton = onClickCancelButton,
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
                mapsMapView  = mapView,
                mapsMapboxMap = mapboxMap,
                onInitializeMapView = onInitializeMapView,
            )

        }


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

            SearchAssistanceButton(
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
                NoInternetScreen(
                    modifier = Modifier.constrainAs(noInternetScreen) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        centerTo(parent)
                        width = Dimension.matchParent
                        height = Dimension.matchParent
                    },
                    onClickRetryButton = onClickNoInternetRetryButton)
            }

    }
}







