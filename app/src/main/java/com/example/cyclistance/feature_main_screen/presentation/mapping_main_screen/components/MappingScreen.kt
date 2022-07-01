package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner

import com.example.cyclistance.common.MappingConstants.CAMERA_TILT_DEGREES
import com.example.cyclistance.common.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.common.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.common.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.common.MappingConstants.MAP_ZOOM
import com.example.cyclistance.common.MappingConstants.MAX_ZOOM_LEVEL_MAPS
import com.example.cyclistance.common.MappingConstants.MIN_ZOOM_LEVEL_MAPS
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.SetupAlertDialog
import com.example.cyclistance.feature_main_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.utils.ConnectionStatus
import com.example.cyclistance.utils.ConnectionStatus.checkLocationSetting
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    isDarkTheme: LiveData<Boolean>,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

//todo: wait for user to click the search button before starting permission
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val state = mappingViewModel.state.value

    BackHandler(enabled = true, onBack = onBackPressed)

    val locationPermissionsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
    val context = LocalContext.current
    var alertDialogState by remember { mutableStateOf(AlertDialogModel()) }
    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Timber.d("GPS Setting Request Accepted")
            mappingViewModel.onEvent(event = MappingEvent.SubscribeToLocationUpdates)
            return@rememberLauncherForActivityResult
        }
        Timber.d("GPS Setting Request Denied")

    }

    val postProfile = {
        if (!ConnectionStatus.hasGPSConnection(context)) {
            checkLocationSetting(
                context = context,
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

        if (locationPermissionsState.allPermissionsGranted) {
            mappingViewModel.onEvent(event = MappingEvent.SubscribeToLocationUpdates)
        }

        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MappingUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is MappingUiEvent.ShowAlertDialog -> {
                    alertDialogState = AlertDialogModel(
                        title = event.title,
                        description = event.description,
                        resId = event.imageResId
                    )
                }
                is MappingUiEvent.ShowNoInternetScreen -> {
                    navigateTo(Screens.NoInternetScreen.route, null)
                }
                is MappingUiEvent.ShowConfirmDetailsScreen -> {
                    navigateTo(Screens.ConfirmDetailsScreen.route, null)
                }
                is MappingUiEvent.ShowSettingScreen -> {
                    navigateTo(Screens.EditProfileScreen.route, null)
                }
                is MappingUiEvent.ShowSignInScreen -> {
                    navigateTo(Screens.SignInScreen.route, Screens.MappingScreen.route)
                }
            }
        }
    }




//todo: add snackbarHost, fab button values, topbar, just use all the parameter values.
    Scaffold(
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                elevation = 10.dp,
                title = { DefaultTitleTopAppBar() },
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground)
                    }
                })
        },
        drawerContent = {
            MappingDrawerContent(
                onSettingsItemClick = {
                    navigateTo(Screens.SettingScreen.route, null)
                },
                onChatItemClick = {
                    /*  todo: open chat screen here   */
                },
                onSignOutItemClick = {
                    mappingViewModel.onEvent(event = MappingEvent.SignOut)
                }
            )
        },
        content = {

            RequestMultiplePermissions(
                multiplePermissionsState = locationPermissionsState, onPermissionGranted = {
                    if (!ConnectionStatus.hasGPSConnection(context)) {
                        checkLocationSetting(
                            context = context,
                            onDisabled = settingResultRequest::launch,
                            onEnabled = {
                                mappingViewModel.onEvent(event = MappingEvent.SubscribeToLocationUpdates)
                            })
                    }
                })

            if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                SetupAlertDialog(
                    alertDialog = alertDialogState,
                    onDismissRequest = {
                        alertDialogState = AlertDialogModel()
                    })
            }


            ConstraintLayout {

                val (mapScreen, searchButton, circularProgressbar) = createRefs()

                MapScreen(
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.constrainAs(mapScreen) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    })

                if (state.findAssistanceButtonVisible) {
                    SearchAssistanceButton(modifier = Modifier.constrainAs(searchButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.value(45.dp)
                    }, onClick = {
                        if (locationPermissionsState.allPermissionsGranted) {
                            mappingViewModel.onEvent(event = MappingEvent.SubscribeToLocationUpdates).also {
                                postProfile()
                            }
                            return@SearchAssistanceButton
                        }

                        if(!locationPermissionsState.shouldShowRationale){
                            Toast.makeText(context, "Location permission is not yet granted.", Toast.LENGTH_SHORT).show()
                            return@SearchAssistanceButton
                        }

                        locationPermissionsState.launchMultiplePermissionRequest()
                    })
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

            }
        })

}

@Composable
fun SearchAssistanceButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier) {
        Text(
            text = "Search for Assistance",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 4.dp,
                start = 12.dp,
                end = 12.dp)
        )
    }
}

@Composable
fun MapScreen(isDarkTheme: LiveData<Boolean>, modifier: Modifier) {

    AndroidView(
        modifier = modifier,
        factory = { context ->

            MapView(context).apply {

                this.getMapAsync { mapboxMap ->
                    with(mapboxMap) {
                        this@apply.findViewTreeLifecycleOwner()?.let { lifeCycleOwner ->
                            isDarkTheme.observe(lifeCycleOwner) { darkTheme ->
                                setStyle(if (darkTheme) Style.TRAFFIC_NIGHT else Style.TRAFFIC_DAY)
                            }
                        }
                        uiSettings.isAttributionEnabled = false
                        uiSettings.isLogoEnabled = false
                        setMaxZoomPreference(MAX_ZOOM_LEVEL_MAPS)
                        setMinZoomPreference(MIN_ZOOM_LEVEL_MAPS)
                        animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE))
                                    .zoom(MAP_ZOOM)
                                    .tilt(CAMERA_TILT_DEGREES)
                                    .build()), DEFAULT_CAMERA_ANIMATION_DURATION)
                    }

                }

            }
        }
    )
}








