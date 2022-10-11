package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Build
import android.os.Build.VERSION_CODES.*
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.location.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingBottomSheet
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingMapsScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.SearchAssistanceButton
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MapUiComponents
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.startServiceIntentAction
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
                        event = MappingEvent.UploadProfile)

                })
        } else {
            mappingViewModel.onEvent(
                event = MappingEvent.UploadProfile)

        }
    }




    LaunchedEffect(key1 = true) {

        with(mappingViewModel) {

            onEvent(event = MappingEvent.GetUsersAsynchronously)
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
        mapUiComponents = mappingViewModel.mapUiComponents,
        state = state,
        onClickRetryButton = {
            if (context.hasInternetConnection()) {
                mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetScreen)
            }
        },
        onClickSearchButton = {
            if (locationPermissionsState.allPermissionsGranted) {
                context.startServiceIntentAction()
                postProfile()

                return@MappingScreen
            }

            if (!locationPermissionsState.shouldShowRationale) {
                Toast.makeText(
                    context,
                    "Location permission is not yet granted.",
                    Toast.LENGTH_SHORT).show()
                return@MappingScreen
            }

            locationPermissionsState.launchMultiplePermissionRequest()
        })

}


@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun MappingScreenPreview() {


    CyclistanceTheme(true) {

        MappingScreen(
            modifier = Modifier,
            isDarkTheme = true,
            state = MappingState(),
            onClickRetryButton = {},
            onClickSearchButton = {},
            mapUiComponents = MapUiComponents(),

            )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MappingScreen(
    modifier: Modifier,
    isDarkTheme: Boolean,
    state: MappingState,
    mapUiComponents: MapUiComponents,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickRetryButton: () -> Unit,
    onClickSearchButton: () -> Unit) {


    val context = LocalContext.current
    val mapView = rememberMapView(context = context)

    val mapboxMap = remember {
        mapView.getMapboxMap()
    }

    val userLocationAvailable = remember(state.latitude, state.longitude) {
        locationPermissionState.allPermissionsGranted.and(state.latitude != DEFAULT_LATITUDE && state.longitude != DEFAULT_LONGITUDE)
    }

    val locateUser =
        {
            if (userLocationAvailable) {
                mapView.location2.apply {
                    enabled = true
                }
                val point = Point.fromLngLat(state.longitude, state.latitude)
                mapboxMap.flyTo(
                    cameraOptions {
                        center(point)
                        zoom(MAP_ZOOM)
                        bearing(0.0)
                    },
                    mapAnimationOptions {
                        duration(DEFAULT_CAMERA_ANIMATION_DURATION)
                    }
                )
            }
        }


    LaunchedEffect(key1 = userLocationAvailable) {
        locateUser()
    }


    MappingBottomSheet(
        isDarkTheme = isDarkTheme,
        onClickActionButton = { /*TODO: add action here, or lambda parameters*/ },
        bottomSheetType = state.bottomSheetType) {

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
                mapUiComponents = mapUiComponents,
                mapView = mapView,
                isDarkTheme = isDarkTheme,
                mapboxMap = mapboxMap,
                locationPermissionState = locationPermissionState
            )

            LocateUserButton(
                modifier = Modifier
                    .size(53.dp)
                    .constrainAs(locateButton) {
                        end.linkTo(parent.end, margin = 5.dp)
                        bottom.linkTo(parent.bottom)
                        centerVerticallyTo(parent)
                    },
                icon = if (locationPermissionState.allPermissionsGranted) Resource.drawable.ic_baseline_my_location_24 else Resource.drawable.ic_locate_user_position,
                onClick = {

                    locationPermissionState.requestPermission(
                        context = context,
                        rationalMessage = "Location permission is not yet granted.") {
                        locateUser()
                    }


                }
            )


            if (state.findAssistanceButtonVisible) {
                SearchAssistanceButton(
                    enabled = !state.isLoading,
                    modifier = Modifier.constrainAs(searchButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.value(45.dp)
                    }, onClickSearchButton = onClickSearchButton)
            }

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
                    onClickRetryButton = onClickRetryButton)
            }
        }

    }


}





