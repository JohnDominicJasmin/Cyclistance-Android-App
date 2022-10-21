package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.rememberMapView
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.startServiceIntentAction
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.locationcomponent.location2
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.cyclistance.R as Resource


@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    typeBottomSheet: String = "",
    isDarkTheme: Boolean,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    scaffoldState: ScaffoldState,
    navController: NavController) {

    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val mapView = rememberMapView(context = context)

    val mapboxMap = remember {
        mapView.getMapboxMap()
    }





    LaunchedEffect(key1 = typeBottomSheet) {
        if (typeBottomSheet.isNotEmpty()) {
            mappingViewModel.onEvent(event = MappingEvent.StartPinging)
        }
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(typeBottomSheet))
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

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            context.startServiceIntentAction()
            Timber.d("GPS Setting Request Accepted")
            return@rememberLauncherForActivityResult
        }
        Timber.d("GPS Setting Request Denied")

    }

    val postProfile = {
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

    val userLocationAvailable by derivedStateOf {
        locationPermissionsState.allPermissionsGranted.and(state.latitude != DEFAULT_LATITUDE && state.longitude != DEFAULT_LONGITUDE)
    }

    val locateUser =
        remember(Unit) {
            { zoomLevel: Double ->
                if (userLocationAvailable) {
                    mapView.location2.apply {
                        enabled = true
                    }
                    val point = Point.fromLngLat(state.longitude, state.latitude)
                    mapboxMap.flyTo(
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


    LaunchedEffect(key1 = userLocationAvailable) {
        locateUser(DEFAULT_MAP_ZOOM_LEVEL)
    }



    LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            context.startServiceIntentAction()
        }
    }

    LaunchedEffect(key1 = state.drawableImages.userDrawableImage){
        mapView.location2.apply {


            if(state.drawableImages.userDrawableImage != null){

                locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(context, Resource.drawable.ic_bearing_image_large),
                    topImage = state.drawableImages.userDrawableImage,
                    shadowImage = ContextCompat.getDrawable(context, com.mapbox.maps.R.drawable.mapbox_user_icon_shadow)
                )

                return@apply
            }

            locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(context, Resource.drawable.ic_bearing_image_small),
                topImage = ContextCompat.getDrawable(context, Resource.drawable.ic_location_top_image_small),
                shadowImage = ContextCompat.getDrawable(context, com.mapbox.maps.R.drawable.mapbox_user_icon_shadow)
            )




        }
    }

    LaunchedEffect(key1 = true) {

        with(mappingViewModel) {
            onEvent(event = MappingEvent.GetUsers)
            onEvent(event = MappingEvent.SubscribeToLocationUpdates)

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


                }
            }
        }
    }


    RequestMultiplePermissions(
        multiplePermissionsState = locationPermissionsState, onPermissionGranted = {
            if (!context.hasGPSConnection()) {
                context.checkLocationSetting(
                    onDisabled = settingResultRequest::launch,
                    onEnabled = {
                        context.startServiceIntentAction()
                    })
            }
        })


    MappingScreen(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        onClickNoInternetRetryButton = {
            if (context.hasInternetConnection()) {
                mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetScreen)
            }
        },
        onClickSearchButton = {
            locationPermissionsState.requestPermission(
                context = context,
                rationalMessage = "Location permission is not yet granted.") {
                context.startServiceIntentAction()
                postProfile()
            }
        },
        locationPermissionState = locationPermissionsState,
        onClickLocateUserButton = {
            locationPermissionsState.requestPermission(
                context = context,
                rationalMessage = "Location permission is not yet granted.") {
                locateUser(LOCATE_USER_ZOOM_LEVEL)
            }
        },
        mapboxMap = mapboxMap,
        mapView = mapView,
        onClickCancelSearchButton = {
            //todo: update user profile to searching = false and show search assistance button
        }
    )

}


@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun MappingScreenPreview() {
    val context = LocalContext.current
    val mapView = rememberMapView(context = context)

    val mapboxMap = remember {
        mapView.getMapboxMap()
    }

    CyclistanceTheme(true) {

        MappingScreen(
            modifier = Modifier,
            isDarkTheme = true,
            state = MappingState(),
            onClickNoInternetRetryButton = {},
            onClickSearchButton = {},
            onClickLocateUserButton = {},
            mapView = mapView,
            mapboxMap = mapboxMap
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingScreen(
    modifier: Modifier,
    isDarkTheme: Boolean,
    state: MappingState,
    mapView: MapView,
    mapboxMap: MapboxMap,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickNoInternetRetryButton: () -> Unit = {},
    onClickLocateUserButton: () -> Unit = {},
    onClickSearchButton: () -> Unit = {},
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = { },
    onClickCallButton: () -> Unit = {},
    onClickChatButton: () -> Unit = {},
    onClickCancelButton: () -> Unit = {},


    ) {

    val configuration = LocalConfiguration.current
    MappingBottomSheet(
        isDarkTheme = isDarkTheme,
        bottomSheetType = state.bottomSheetType,
        onClickRescueArrivedButton = onClickRescueArrivedButton,
        onClickReachedDestinationButton = onClickReachedDestinationButton,
        onClickCancelSearchButton = onClickCancelSearchButton,
        onClickCallButton = onClickCallButton,
        onClickChatButton = onClickChatButton,
        onClickCancelButton = onClickCancelButton) {

        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()) {

            val (mapScreen, searchButton, circularProgressbar, noInternetScreen, locateButton) = createRefs()

            MappingMapsScreen(
                state = state,
                modifier = Modifier.constrainAs(mapScreen) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                isDarkTheme = isDarkTheme,
                locationPermissionState = locationPermissionState,
                mapView = mapView,
                mapboxMap = mapboxMap
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

            SearchAssistanceButton(
                enabled = !state.isLoading,
                modifier = Modifier.constrainAs(searchButton) {
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.value(45.dp)
                    width = Dimension.wrapContent
                }, onClickSearchButton = onClickSearchButton,
                buttonVisible = state.findAssistanceButtonVisible
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


}






