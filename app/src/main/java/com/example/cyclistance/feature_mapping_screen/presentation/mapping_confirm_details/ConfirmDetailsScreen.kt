package com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.cyclistance.core.utils.ConnectionStatus
import com.example.cyclistance.core.utils.MappingConstants.SEARCH_BOTTOM_SHEET
import com.example.cyclistance.feature_mapping_screen.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.AddressTextField
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.ButtonDescriptionDetails
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.components.DropDownBikeList
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.*
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ConfirmDetailsScreen(
    viewModel: ConfirmDetailsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ConfirmDetailsUiEvent.ShowMappingScreen -> {
                    navController.navigateScreenInclusively(
                        destination = Screens.MappingScreen.route + "?bottomSheetType=$SEARCH_BOTTOM_SHEET",
                        popUpToDestination = Screens.ConfirmDetailsScreen.route)
                }

                is ConfirmDetailsUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    CoinDetailScreen(modifier = Modifier.padding(paddingValues), state = state,

        onValueChangeAddress = { address ->
            viewModel.onEvent(event = ConfirmDetailsEvent.EnterAddress(address.trim()))
        },
        onValueChangeMessage = { message ->
            viewModel.onEvent(event = ConfirmDetailsEvent.EnterMessage(message))
        },
        onClickBikeType = { bikeType ->
            viewModel.onEvent(event = ConfirmDetailsEvent.SelectBikeType(bikeType))
        },
        onClickDescriptionButton = { description ->
            viewModel.onEvent(event = ConfirmDetailsEvent.SelectDescription(description))
        },
        onClickCancelButton = {
            navController.popBackStack()
        },
        onClickConfirmButton = {
            viewModel.onEvent(event = ConfirmDetailsEvent.Save)
        },
        onClickRetryButton = {
            if (ConnectionStatus.hasInternetConnection(context)) {
                viewModel.onEvent(event = ConfirmDetailsEvent.DismissNoInternetScreen)
            }
        })
}

@Preview
@Composable
fun CoinDetailScreenPreview() {
    CyclistanceTheme(true) {
        CoinDetailScreen(modifier = Modifier, state = ConfirmDetailsState(isLoading = true))
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
    onClickRetryButton: () -> Unit = {},

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

            Text(
                text = "This will send a request to nearby bikers.",
                color = Black440,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.constrainAs(noteText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttonNavButtonSection.top)
                }
            )

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
                NoInternetScreen(
                    modifier = Modifier.constrainAs(noInternetScreen) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        centerTo(parent)
                        width = Dimension.matchParent
                        height = Dimension.matchParent
                    }, onClickRetryButton = onClickRetryButton)
            }
        }

    }

}





