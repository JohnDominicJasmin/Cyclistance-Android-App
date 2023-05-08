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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetailsModel
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.AddressTextField
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.ButtonDescriptionDetails
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.DropDownBikeList
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ConfirmDetailsScreen(
    viewModel: ConfirmDetailsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var address by rememberSaveable { mutableStateOf("") }
    var addressErrorMessage by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var bikeType by rememberSaveable { mutableStateOf("") }
    var bikeTypeErrorMessage by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var descriptionErrorMessage by rememberSaveable { mutableStateOf("") }
    var isNoInternetDialogVisible by rememberSaveable { mutableStateOf(false) }
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ConfirmDetailsUiEvent.ConfirmDetailsSuccess -> {
                    navController.navigateScreen(Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE=${BottomSheetType.SearchAssistance.type}")
                }

                is ConfirmDetailsUiEvent.UserError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsUiEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is ConfirmDetailsUiEvent.GetSavedBikeType -> {
                    bikeType = event.bikeType
                }

                is ConfirmDetailsUiEvent.GetSavedAddress -> {
                    address = event.address
                }

                is ConfirmDetailsUiEvent.NoInternetConnection -> {
                    isNoInternetDialogVisible = true
                }

                is ConfirmDetailsUiEvent.InvalidBikeType -> {
                    bikeTypeErrorMessage = event.reason
                }

                is ConfirmDetailsUiEvent.InvalidDescription -> {
                    descriptionErrorMessage = event.reason
                }

                is ConfirmDetailsUiEvent.InvalidAddress -> {
                    addressErrorMessage = event.reason
                }

            }
        }
    }


    val onValueChangeAddress = remember {
        { addressInput: String ->
            address = addressInput
            addressErrorMessage = ""
        }
    }
    val onValueChangeMessage = remember {
        { messageInput: String ->
            message = messageInput
        }
    }
    val onClickBikeType = remember {
        { bikeTypeInput: String ->
            bikeType = bikeTypeInput
            bikeTypeErrorMessage = ""

        }
    }
    val onClickDescriptionButton = remember {
        { descriptionInput: String ->
            description = descriptionInput
            descriptionErrorMessage = ""
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
                event = ConfirmDetailsEvent.ConfirmDetails(
                    confirmDetailsModel = ConfirmationDetailsModel(
                        address = address,
                        bikeType = bikeType,
                        description = description,
                        message = message
                    )
                ))
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            alertDialogState = AlertDialogState()
        }
    }

    ConfirmDetailsContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        onValueChangeAddress = onValueChangeAddress,
        onValueChangeMessage = onValueChangeMessage,
        onClickBikeType = onClickBikeType,
        onClickDescriptionButton = onClickDescriptionButton,
        onClickConfirmButton = onClickConfirmButton,
        onClickCancelButton = onClickCancelButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        address = address,
        addressErrorMessage = addressErrorMessage,
        bikeType = bikeType,
        bikeTypeErrorMessage = bikeTypeErrorMessage,
        description = description,
        descriptionErrorMessage = descriptionErrorMessage,
        message = message,
        isNoInternetDialogVisible = isNoInternetDialogVisible
    )
}


@Preview
@Composable
fun PreviewConfirmDetailsScreen() {
    CyclistanceTheme(true) {
        ConfirmDetailsContent(
            modifier = Modifier,
            state = ConfirmDetailsState(),
            address = "Address sample",
            addressErrorMessage = "Invalid Address",
            bikeType = "Mountain Bike",
            bikeTypeErrorMessage = "",
            description = "Flat tire",
            descriptionErrorMessage = "",
            message = "bla bla",
            isNoInternetDialogVisible = false)
    }
}


@Composable
fun ConfirmDetailsContent(
    modifier: Modifier,
    state: ConfirmDetailsState = ConfirmDetailsState(),
    address: String,
    addressErrorMessage: String,
    bikeType: String,
    bikeTypeErrorMessage: String,
    description: String,
    descriptionErrorMessage: String,
    message: String,
    isNoInternetDialogVisible: Boolean,
    onValueChangeAddress: (String) -> Unit = {},
    onClickBikeType: (String) -> Unit = {},
    onClickDescriptionButton: (String) -> Unit = {},
    onValueChangeMessage: (String) -> Unit = {},
    onClickCancelButton: () -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {},

    ) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {


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
                address = address,
                addressErrorMessage = addressErrorMessage,
                onValueChange = onValueChangeAddress,
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
                errorMessage = bikeTypeErrorMessage,
                selectedItem = bikeType,
                onClickItem = onClickBikeType,
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
                selectedOption = description,
                errorMessage = descriptionErrorMessage,
                onClickButton = onClickDescriptionButton,
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
                message = message,
                onChangeValueMessage = onValueChangeMessage,
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
                onClickCancelButton = onClickCancelButton,
                onClickConfirmButton = onClickConfirmButton,
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
            if (isNoInternetDialogVisible) {
                NoInternetDialog(
                    onDismiss = onDismissNoInternetDialog,
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





