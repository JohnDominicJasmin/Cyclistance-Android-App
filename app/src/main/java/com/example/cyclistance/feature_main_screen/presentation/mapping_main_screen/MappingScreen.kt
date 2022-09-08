package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.ConnectionStatus
import com.example.cyclistance.core.utils.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_main_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.*
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    isDarkTheme: LiveData<Boolean?>,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {


    val state by mappingViewModel.state.collectAsState()


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
                is MappingUiEvent.ShowNoInternetScreen -> {
                    navController.navigateScreen(Screens.NoInternetScreen.route, Screens.MappingScreen.route)
                }
                is MappingUiEvent.ShowConfirmDetailsScreen -> {
                    navController.navigateScreen(Screens.ConfirmDetailsScreen.route, Screens.MappingScreen.route)
                }
                is MappingUiEvent.ShowEditProfileScreen -> {
                    navController.navigateScreen(Screens.EditProfileScreen.route, Screens.MappingScreen.route)
                }
                is MappingUiEvent.ShowSignInScreen -> {
                    navController.navigateScreen(Screens.SignInScreen.route, Screens.MappingScreen.route)
                }
            }
        }
    }


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


            ConstraintLayout(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

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


}












