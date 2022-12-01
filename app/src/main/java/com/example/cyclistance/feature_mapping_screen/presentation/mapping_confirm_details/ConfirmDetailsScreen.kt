package com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_mapping_screen.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.AddressTextField
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.ButtonDescriptionDetails
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.DropDownBikeList
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.BottomSheetType
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ConfirmDetailsScreen(
    hasInternetConnection : Boolean,
    viewModel: ConfirmDetailsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ConfirmDetailsUiEvent.ShowMappingScreen -> {
                    navController.navigateScreen(Screens.MappingScreen.route + "?bottomSheetType=${BottomSheetType.SearchAssistance.type}")
                }

                is ConfirmDetailsUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    val onValueChangeAddress = remember {{ address: String ->
        viewModel.onEvent(event = ConfirmDetailsEvent.EnterAddress(address))
    }}
    val onValueChangeMessage = remember {{ message: String ->
        viewModel.onEvent(event = ConfirmDetailsEvent.EnterMessage(message))
    }}
    val onClickBikeType = remember {{ bikeType: String ->
        viewModel.onEvent(event = ConfirmDetailsEvent.SelectBikeType(bikeType))
        viewModel.onEvent(event = ConfirmDetailsEvent.ClearBikeTypeErrorMessage)

    }}
    val onClickDescriptionButton = remember {{ description: String ->
        viewModel.onEvent(event = ConfirmDetailsEvent.SelectDescription(description))
        viewModel.onEvent(event = ConfirmDetailsEvent.ClearDescriptionErrorMessage)
    }}
    val onClickCancelButton = remember {{
        navController.navigateScreen(Screens.MappingScreen.route)
    }}
    val onClickConfirmButton = remember {{
            viewModel.onEvent(event = ConfirmDetailsEvent.ConfirmDetails)
    }}
    val onDismissNoInternetDialog = remember{ {
            viewModel.onEvent(event = ConfirmDetailsEvent.DismissNoInternetDialog)
    }}

    CoinDetailScreen(modifier = Modifier.padding(paddingValues),
        state = state,
        onValueChangeAddress = onValueChangeAddress,
        onValueChangeMessage = onValueChangeMessage,
        onClickBikeType = onClickBikeType,
        onClickDescriptionButton = onClickDescriptionButton,
        onClickConfirmButton = onClickConfirmButton,
        onClickCancelButton = onClickCancelButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog)
}


@Preview
@Composable
fun CoinDetailScreenPreview() {
    CyclistanceTheme(true) {
        CoinDetailScreen(modifier = Modifier, state = ConfirmDetailsState())
    }
}


@Composable
fun CoinDetailScreen(
    modifier: Modifier,
    state: ConfirmDetailsState = ConfirmDetailsState(),
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
                    .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = true)
                    .constrainAs(addressTextField) {
                        top.linkTo(parent.top, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width = Dimension.percent(0.9f)
                        height = Dimension.wrapContent
                    },
                address = state.address,
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
                errorMessage = state.bikeTypeErrorMessage,
                selectedItem = state.bikeType,
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
                selectedOption = state.description,
                errorMessage = state.descriptionErrorMessage,
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
                message = state.message,
                onValueChange = onValueChangeMessage,
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
            if (!state.hasInternet) {
                NoInternetDialog(onDismiss = onDismissNoInternetDialog, modifier = Modifier.constrainAs(noInternetScreen) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent})

            }
        }

    }

}





