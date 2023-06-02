package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import com.example.cyclistance.feature_dialogs.presentation.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetails
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.AddressTextField
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.ButtonDescriptionDetails
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.DropDownBikeList
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsState
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
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


@Preview
@Composable
fun PreviewConfirmDetailsScreen() {
    CyclistanceTheme(true) {
        ConfirmDetailsContent(
            modifier = Modifier,
            state = ConfirmDetailsState(),
            uiState = ConfirmDetailsUiState())
    }
}


@Composable
fun ConfirmDetailsContent(
    modifier: Modifier,
    state: ConfirmDetailsState = ConfirmDetailsState(),
    uiState: ConfirmDetailsUiState = ConfirmDetailsUiState(),
    event: (ConfirmDetailsUiEvent) -> Unit = {}) {


    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(rememberScrollState())) {


            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {

                val (addressTextField, bikeTypeDropDownList, buttonDescriptionSection, additionalMessageSection, buttonNavButtonSection, noteText, noInternetScreen, circularProgressBar) = createRefs()

                AddressTextField(
                    modifier = Modifier
                        .constrainAs(addressTextField) {
                            top.linkTo(parent.top, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            width = Dimension.percent(0.9f)
                            height = Dimension.wrapContent
                        },
                    address = uiState.address,
                    addressErrorMessage = uiState.addressErrorMessage,
                    onValueChange = { event(ConfirmDetailsUiEvent.ChangeAddress(it)) },
                    enabled = !state.isLoading
                )

                DropDownBikeList(
                    modifier = Modifier
                        .constrainAs(bikeTypeDropDownList) {
                            top.linkTo(addressTextField.bottom, margin = 10.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            width = Dimension.percent(0.9f)
                            height = Dimension.wrapContent
                        },
                    errorMessage = uiState.bikeTypeErrorMessage,
                    selectedItem = uiState.bikeType,
                    onClickItem = {
                        event(ConfirmDetailsUiEvent.ChangeBikeType(it))
                    },
                    enabled = !state.isLoading)

                ButtonDescriptionDetails(
                    modifier = Modifier
                        .wrapContentHeight()
                        .constrainAs(buttonDescriptionSection) {
                            top.linkTo(bikeTypeDropDownList.bottom, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.9f)
                        },
                    selectedOption = uiState.description,
                    errorMessage = uiState.descriptionErrorMessage,
                    onClickButton = {
                        event(ConfirmDetailsUiEvent.ChangeDescription(it))
                    },
                    state = state,
                )

                AdditionalMessage(
                    modifier = Modifier
                        .constrainAs(additionalMessageSection) {
                            top.linkTo(buttonDescriptionSection.bottom, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            height = Dimension.percent(0.25f)
                            width = Dimension.percent(0.9f)

                        },
                    message = uiState.message,
                    onChangeValueMessage = {
                        event(ConfirmDetailsUiEvent.ChangeMessage(it))
                    },
                    enabled = !state.isLoading
                )

                Row(
                    modifier = Modifier.constrainAs(noteText) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttonNavButtonSection.top)
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info icon",
                        tint = Black440,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 2.dp)

                    )

                    Text(
                        text = "Your location will be shared with Rescuer",
                        color = Black440,
                        style = MaterialTheme.typography.caption,

                        )
                }

                MappingButtonNavigation(
                    modifier = Modifier
                        .constrainAs(buttonNavButtonSection) {
                            top.linkTo(additionalMessageSection.bottom, margin = 50.dp)
                            bottom.linkTo(parent.bottom, margin = 12.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.9f)
                        },
                    onClickCancelButton = {
                        event(ConfirmDetailsUiEvent.CancelConfirmation)
                    },
                    onClickConfirmButton = {
                        event(ConfirmDetailsUiEvent.ConfirmDetails)
                    },
                    negativeButtonEnabled = !state.isLoading,
                    positiveButtonEnabled = !state.isLoading)


                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .constrainAs(circularProgressBar) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                centerTo(parent)
                            }
                    )
                }
                if (uiState.isNoInternetVisible) {
                    NoInternetDialog(
                        onDismiss = { event(ConfirmDetailsUiEvent.DismissNoInternetDialog) },
                        modifier = Modifier.constrainAs(noInternetScreen) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        })

                }
            }

        }
    }

}





