package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetails
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.ConfirmDetailsContent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun ConfirmDetailsScreen(
    viewModel: ConfirmDetailsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var uiState by rememberSaveable { mutableStateOf(ConfirmDetailsUiState()) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.distinctUntilChanged().collect { event ->
            when (event) {
                is ConfirmDetailsEvent.ConfirmDetailsSuccess -> {
                    navController.navigateScreen(Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE=${BottomSheetType.SearchAssistance.type}")
                }

                is ConfirmDetailsEvent.UserError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsEvent.GetSavedBikeType -> {
                    uiState = uiState.copy(bikeType = event.bikeType)
                }

                is ConfirmDetailsEvent.GetSavedAddress -> {
                    uiState = uiState.copy(address = event.address)
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
        { addressInput: String ->
            uiState = uiState.copy(
                address = addressInput,
                addressErrorMessage = ""
            )
        }
    }
    val onValueChangeMessage = remember {
        { messageInput: String ->
            uiState = uiState.copy(
                message = messageInput
            )
        }
    }
    val onClickBikeType = remember {
        { bikeTypeInput: String ->
            uiState = uiState.copy(
                bikeType = bikeTypeInput,
                bikeTypeErrorMessage = ""
            )
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
            navController.navigateScreen(Screens.MappingScreen.route)
        }
    }
    val onClickConfirmButton = remember {
        {
            viewModel.onEvent(
                event = ConfirmDetailsVmEvent.ConfirmDetails(
                    confirmDetailsModel = ConfirmationDetails(
                        address = uiState.address,
                        bikeType = uiState.bikeType,
                        description = uiState.description,
                        message = uiState.message
                    )
                ))
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    ConfirmDetailsContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        event = { event ->
            when (event) {
                is ConfirmDetailsUiEvent.ChangeAddress -> {
                    onValueChangeAddress(event.address)
                }

                is ConfirmDetailsUiEvent.ChangeBikeType -> {
                    onClickBikeType(event.bikeType)
                }

                is ConfirmDetailsUiEvent.ChangeDescription -> {
                    onClickDescriptionButton(event.description)
                }

                is ConfirmDetailsUiEvent.ChangeMessage -> {
                    onValueChangeMessage(event.message)
                }

                is ConfirmDetailsUiEvent.ConfirmDetails -> {
                    onClickConfirmButton()
                }

                is ConfirmDetailsUiEvent.CancelConfirmation -> {
                    onClickCancelButton()
                }

                is ConfirmDetailsUiEvent.DismissNoInternetDialog -> {
                    onDismissNoInternetDialog()
                }
            }
        }
    )
}




