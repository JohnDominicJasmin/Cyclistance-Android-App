package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import android.Manifest
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.permissions.requestPermission
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetails
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.ConfirmDetailsContent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState


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
                }

                is ConfirmDetailsEvent.InvalidDescription -> {
                    uiState = uiState.copy(descriptionErrorMessage = event.reason)
                }

                is ConfirmDetailsEvent.InvalidAddress -> {
                    uiState = uiState.copy(addressErrorMessage = event.reason)

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

    val onClickConfirmButton = remember {
        {

            if (Build.VERSION.SDK_INT >= Q) {


                foregroundLocationPermissionsState.requestPermission(onGranted = {
                    backgroundLocationPermissionState.requestPermission(onGranted = {
                        viewModel.onEvent(
                            event = ConfirmDetailsVmEvent.ConfirmDetails(
                                confirmDetailsModel = ConfirmationDetails(
                                    address = address.text,
                                    bikeType = bikeType.text,
                                    description = uiState.description,
                                    message = message.text
                                )
                            ))
                    }, onExplain = {
                        uiState = uiState.copy(
                            backgroundLocationPermissionDialogVisible = true,
                            foregroundLocationPermissionDialogVisible = false)
                    })

                }, onExplain = {
                    uiState = uiState.copy(
                        foregroundLocationPermissionDialogVisible = true,
                        backgroundLocationPermissionDialogVisible = false)
                })


            } else {
                foregroundLocationPermissionsState.requestPermission(
                    onGranted = {
                        viewModel.onEvent(
                            event = ConfirmDetailsVmEvent.ConfirmDetails(
                                confirmDetailsModel = ConfirmationDetails(
                                    address = address.text,
                                    bikeType = bikeType.text,
                                    description = uiState.description,
                                    message = message.text
                                )
                            ))
                    },
                    onExplain = {
                        uiState = uiState.copy(
                            foregroundLocationPermissionDialogVisible = true,
                            backgroundLocationPermissionDialogVisible = false)
                    }
                )
            }


        }
    }
    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val onDismissBackgroundLocationDialog = remember {
        {
            uiState = uiState.copy(backgroundLocationPermissionDialogVisible = false)
        }
    }

    val onDismissForegroundLocationDialog = remember {
        {
            uiState = uiState.copy(foregroundLocationPermissionDialogVisible = false)
        }
    }



    ConfirmDetailsContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        uiState = uiState,
        bikeType = bikeType,
        message = message,
        address = address,
        event = { event ->
            when (event) {
                is ConfirmDetailsUiEvent.OnChangeAddress -> onValueChangeAddress(event.address)
                is ConfirmDetailsUiEvent.OnChangeBikeType -> onClickBikeType(event.bikeType)
                is ConfirmDetailsUiEvent.OnChangeDescription -> onClickDescriptionButton(event.description)
                is ConfirmDetailsUiEvent.OnChangeMessage -> onValueChangeMessage(event.message)
                is ConfirmDetailsUiEvent.ConfirmDetails -> onClickConfirmButton()
                is ConfirmDetailsUiEvent.CancelConfirmation -> onClickCancelButton()
                is ConfirmDetailsUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is ConfirmDetailsUiEvent.DismissBackgroundLocationDialog -> onDismissBackgroundLocationDialog()
                is ConfirmDetailsUiEvent.DismissForegroundLocationDialog -> onDismissForegroundLocationDialog()
            }
        }
    )
}




