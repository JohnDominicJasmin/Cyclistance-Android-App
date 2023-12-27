package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.checkLocationSetting
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.hasGPSConnection
import com.myapp.cyclistance.core.utils.contexts.startLocationServiceIntentAction
import com.myapp.cyclistance.core.utils.permissions.isGranted
import com.myapp.cyclistance.core.utils.permissions.requestPermission
import com.myapp.cyclistance.feature_mapping.domain.model.ConfirmationDetails
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.ConfirmDetailsContent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsVmEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConfirmDetailsScreen(
    viewModel: ConfirmDetailsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var uiState by rememberSaveable { mutableStateOf(ConfirmDetailsUiState()) }

    var bikeType by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var message by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var address by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

    val backgroundLocationPermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ConfirmDetailsEvent.ConfirmDetailsSuccess -> {
                    navController.popBackStack()
                }

                is ConfirmDetailsEvent.UserError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsEvent.GetSavedBikeType -> {
                    bikeType = TextFieldValue(event.bikeType)
                }

                is ConfirmDetailsEvent.GetSavedAddress -> {
                    address = TextFieldValue(event.address)
                }

                is ConfirmDetailsEvent.NoInternetConnection -> {
                    uiState = uiState.copy(isNoInternetVisible = true)
                }

                is ConfirmDetailsEvent.InvalidBikeType -> {
                    uiState = uiState.copy(bikeTypeErrorMessage = event.reason)
                    coroutineScope.launch {
                        scrollState.animateScrollTo(10)
                    }
                }

                is ConfirmDetailsEvent.InvalidDescription -> {
                    uiState = uiState.copy(descriptionErrorMessage = event.reason)
                    coroutineScope.launch {
                        scrollState.animateScrollTo(350)
                    }
                }

                is ConfirmDetailsEvent.InvalidAddress -> {
                    uiState = uiState.copy(addressErrorMessage = event.reason)
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }

                }

            }
        }
    }


    val onValueChangeAddress = remember {
        { addressInput: TextFieldValue ->
            uiState = uiState.copy(
                addressErrorMessage = ""
            )
            address = addressInput
        }
    }
    val onValueChangeMessage = remember {
        { messageInput: TextFieldValue ->
            message = messageInput
        }
    }
    val onClickBikeType = remember {
        { bikeTypeInput: TextFieldValue ->
            uiState = uiState.copy(bikeTypeErrorMessage = "")
            bikeType = bikeTypeInput
        }
    }
    val onClickDescriptionButton = remember {
        { descriptionInput: String ->

            uiState = uiState.copy(
                description = descriptionInput,
                descriptionErrorMessage = ""
            )
        }
    }

    val onClickCancelButton = remember {
        {
            navController.popBackStack()
        }
    }

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            context.startLocationServiceIntentAction()
            Timber.d("GPS Setting Request Accepted")
            return@rememberLauncherForActivityResult
        }
        Timber.d("GPS Setting Request Denied")
    }

    val onConfirmDetails = remember{{
        viewModel.onEvent(
            event = ConfirmDetailsVmEvent.ConfirmDetails(
                confirmDetailsModel = ConfirmationDetails(
                    address = address.text,
                    bikeType = bikeType.text,
                    description = uiState.description,
                    message = message.text
                )
            ))
    }}


    val confirmDetails = remember{{
        if(!context.hasGPSConnection()){
            context.checkLocationSetting(
                onDisabled = settingResultRequest::launch,
                onEnabled = {
                    onConfirmDetails()
                })
        }else{
            onConfirmDetails()
        }

    }}

    fun onClickConfirmButton() {


        if(!foregroundLocationPermissionsState.allPermissionsGranted){
            uiState = uiState.copy(
                prominentLocationDialogVisible = true)
            return
        }

        if (Build.VERSION.SDK_INT >= Q) {

            if(!backgroundLocationPermissionState.hasPermission){
                uiState = uiState.copy(
                    prominentLocationDialogVisible = true)
                return
            }

            confirmDetails()

            return
        }


        confirmDetails()

    }


    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val dismissBackgroundLocationDialog = remember {
        {
            uiState = uiState.copy(backgroundLocationPermissionDialogVisible = false)
        }
    }

    val dismissForegroundLocationDialog = remember {
        {
            uiState = uiState.copy(foregroundLocationPermissionDialogVisible = false)
        }
    }

    val dismissProminentLocationDialog = remember {
        {
            uiState = uiState.copy(
                prominentLocationDialogVisible = false
            )
        }
    }



    val requestBackgroundLocationPermission = remember{{
        backgroundLocationPermissionState.requestPermission(onGranted = {
            confirmDetails()
        }, onDenied = {
            uiState = uiState.copy(
                backgroundLocationPermissionDialogVisible = true)
        })

    }}

    val allowProminentLocationDialog = remember {
        {
            if (Build.VERSION.SDK_INT >= Q) {

                foregroundLocationPermissionsState.requestPermission(onGranted = {
                    requestBackgroundLocationPermission()
                }, onDenied = {
                    uiState = uiState.copy(
                        foregroundLocationPermissionDialogVisible = true)
                })


            } else {

                foregroundLocationPermissionsState.requestPermission(
                    onGranted = {
                      confirmDetails()
                    },
                    onDenied = {
                        uiState = uiState.copy(
                            foregroundLocationPermissionDialogVisible = true)
                    }
                )
            }
        }
    }

    LaunchedEffect(key1 = foregroundLocationPermissionsState.isGranted()){
        if(!foregroundLocationPermissionsState.isGranted()){
            return@LaunchedEffect
        }

        if(Build.VERSION.SDK_INT >= Q){
            requestBackgroundLocationPermission()
            return@LaunchedEffect

        }
        confirmDetails()
    }

    LaunchedEffect(key1 = backgroundLocationPermissionState.isGranted(),){
        if(!backgroundLocationPermissionState.isGranted()){
            return@LaunchedEffect
        }
        confirmDetails()
    }


    ConfirmDetailsContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        uiState = uiState,
        bikeType = bikeType,
        message = message,
        address = address,
        scrollState = scrollState,
        event = { event ->
            when (event) {
                is ConfirmDetailsUiEvent.OnChangeAddress -> onValueChangeAddress(event.address)
                is ConfirmDetailsUiEvent.OnChangeBikeType -> onClickBikeType(event.bikeType)
                is ConfirmDetailsUiEvent.OnChangeDescription -> onClickDescriptionButton(event.description)
                is ConfirmDetailsUiEvent.OnChangeMessage -> onValueChangeMessage(event.message)
                is ConfirmDetailsUiEvent.ConfirmDetails -> onClickConfirmButton()
                is ConfirmDetailsUiEvent.CancelConfirmation -> onClickCancelButton()
                is ConfirmDetailsUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is ConfirmDetailsUiEvent.DismissBackgroundLocationDialog -> dismissBackgroundLocationDialog()
                is ConfirmDetailsUiEvent.DismissForegroundLocationDialog -> dismissForegroundLocationDialog()
                ConfirmDetailsUiEvent.AllowProminentLocationDialog -> allowProminentLocationDialog()
                ConfirmDetailsUiEvent.DismissProminentLocationDialog -> dismissProminentLocationDialog()
            }
        }
    )
}




