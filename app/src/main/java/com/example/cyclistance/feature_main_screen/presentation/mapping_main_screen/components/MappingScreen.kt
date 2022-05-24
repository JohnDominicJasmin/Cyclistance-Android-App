package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Build
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout

import com.example.cyclistance.common.MappingConstants.CAMERA_TILT_DEGREES
import com.example.cyclistance.common.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.common.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.common.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.common.MappingConstants.MAP_ZOOM
import com.example.cyclistance.common.MappingConstants.MAX_ZOOM_LEVEL_MAPS
import com.example.cyclistance.common.MappingConstants.MIN_ZOOM_LEVEL_MAPS
import com.example.cyclistance.feature_main_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.theme.ThemeColor
import com.example.cyclistance.utils.ConnectionStatus
import com.example.cyclistance.utils.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.utils.LastLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.maps.Style
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    onBackPressed: () -> Unit,
    navigateTo: (destination: String) -> Unit) {


    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))

    val context = LocalContext.current

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Timber.d("appDebug", "Accepted")

        }else {
            Timber.d("appDebug", "Denied")
        }
    }


    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true, onBack = onBackPressed)
   
    val multiplePermissionsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    } else {
        rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    Scaffold(
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBarCreator(
                icon = Icons.Filled.Menu,
                onClickIcon = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {

                DefaultTitleTopAppBar()
            }
        },
        drawerContent = { MappingDrawerContent() },
        content = {

            RequestMultiplePermissions(multiplePermissionsState = multiplePermissionsState, onPermissionGranted = {
                if (!ConnectionStatus.hasGPSConnection(context)) {
                    checkLocationSetting(
                        context = context,
                        onDisabled = settingResultRequest::launch,
                        onEnabled = {
                            Timber.d("GPS IS TURNED ON!")

                        })
                }else{
                    LastLocation.getUserLocation(context)
                }
            })



            ConstraintLayout() {

                val (mapScreen, searchButton) = createRefs()

                MapScreen(modifier = Modifier.constrainAs(mapScreen) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })


                Button(
                    onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()

                        if (multiplePermissionsState.allPermissionsGranted) {
                            if (!ConnectionStatus.hasGPSConnection(context)) {
                                checkLocationSetting(
                                    context = context,
                                    onDisabled = settingResultRequest::launch,
                                    onEnabled = {
                                        Timber.d("GPS IS TURNED ON!")

                                    })
                            }else{
                                LastLocation.getUserLocation(context)
                            }

                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ThemeColor),
                    modifier = Modifier.constrainAs(searchButton) {
                        bottom.linkTo(parent.bottom, margin = 18.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }) {
                    Text(
                        text = "Search for Assistance",
                        color = Color.Black,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 4.dp,
                            start = 12.dp,
                            end = 12.dp)
                    )
                }

            }
        })

}



@Composable
fun MapScreen(modifier: Modifier) {


    AndroidView(
        modifier = modifier,
        factory = { context ->

            MapView(context).apply {

                this.getMapAsync { mapboxMap ->
                    with(mapboxMap) {
                        setStyle(Style.TRAFFIC_NIGHT)

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








