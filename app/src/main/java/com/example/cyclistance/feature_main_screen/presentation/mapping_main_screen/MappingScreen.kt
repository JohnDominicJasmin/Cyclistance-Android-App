package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.cyclistance.core.utils.ConnectionStatus
import com.example.cyclistance.core.utils.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.feature_main_screen.presentation.common.RequestMultiplePermissions
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.MappingBottomSheet
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.MappingMapsScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.SearchAssistanceButton
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
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


    val state by mappingViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = typeBottomSheet) {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(typeBottomSheet))
    }

    BackHandler(enabled = true, onBack = {
        coroutineScope.launch {
            if (scaffoldState.drawerState.isOpen) {
                scaffoldState.drawerState.close()
            } else {
                navController.popBackStack()
            }
        }
    })

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
            mappingViewModel.onEvent(event = MappingEvent.LocationPermissionGranted)
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
            mappingViewModel.onEvent(event = MappingEvent.LocationPermissionGranted)
        }

        mappingViewModel.eventFlow.collectLatest { event ->
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
                    navController.navigateScreen(
                        Screens.SignInScreen.route,
                        Screens.MappingScreen.route)
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
                        mappingViewModel.onEvent(event = MappingEvent.LocationPermissionGranted)
                    })
            }
        })


    MappingScreen(
        modifier = Modifier.padding(paddingValues),
        isDarkTheme = isDarkTheme,
        state = state,
        onClickRetryButton = {
            if (ConnectionStatus.hasInternetConnection(context)) {
                mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetScreen)
            }
        },
        onClickSearchButton = {
            if (locationPermissionsState.allPermissionsGranted) {
                mappingViewModel.onEvent(event = MappingEvent.LocationPermissionGranted).also {
                    postProfile()
                }
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
        }, onNewLocationResult = {
            mappingViewModel.onEvent(event = MappingEvent.OnLocationChange(it))
        })

}


@Preview
@Composable
fun MappingScreenPreview() {
    val context = LocalContext.current


    CyclistanceTheme(true) {

        MappingScreen(
            modifier = Modifier,
            isDarkTheme = true,
            state = MappingState(),
            onClickRetryButton = {},
            onClickSearchButton = {},
            onNewLocationResult = {},
        )
    }
}


@Composable
fun MappingScreen(
    modifier: Modifier,
    isDarkTheme: Boolean,
    state: MappingState,
    onClickRetryButton: () -> Unit,
    onClickSearchButton: () -> Unit,
    onNewLocationResult: (Location) -> Unit) {


    MappingBottomSheet(
        isDarkTheme = isDarkTheme,
        onClickActionButton = { /*TODO*/ },
        bottomSheetType = state.bottomSheetType) {

        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()) {

            val (mapScreen, searchButton, circularProgressbar, noInternetScreen) = createRefs()

            MappingMapsScreen(
                state = state,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.constrainAs(mapScreen) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                onNewLocationResult = onNewLocationResult)


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








