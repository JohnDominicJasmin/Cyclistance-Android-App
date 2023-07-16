package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_dialogs.presentation.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogBackgroundLocationPermission
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogForegroundLocationPermission
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsState
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme

@Preview(device = "id:pixel_xl")
@Composable
fun PreviewConfirmDetailsScreenDark() {
    CyclistanceTheme(true) {
        ConfirmDetailsContent(
            modifier = Modifier,
            state = ConfirmDetailsState(),
            uiState = ConfirmDetailsUiState(),
            bikeType = TextFieldValue(""),
            message = TextFieldValue(""),
            address = TextFieldValue(""),
        )
    }
}

@Preview(device = "id:pixel_xl")
@Composable
fun PreviewConfirmDetailsScreenLight() {
    CyclistanceTheme(false) {
        ConfirmDetailsContent(
            modifier = Modifier,
            state = ConfirmDetailsState(),
            uiState = ConfirmDetailsUiState(backgroundLocationPermissionDialogVisible = true),
            bikeType = TextFieldValue(""),
            message = TextFieldValue(""),
            address = TextFieldValue(""),
        )
    }
}


@Composable
fun ConfirmDetailsContent(
    modifier: Modifier,
    state: ConfirmDetailsState = ConfirmDetailsState(),
    bikeType: TextFieldValue,
    message: TextFieldValue,
    address: TextFieldValue,
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

                val (addressTextField, bikeTypeDropDownList, buttonDescriptionSection, additionalMessageSection, buttonNavButtonSection, noteText, noInternetScreen, circularProgressBar, permissionDialog) = createRefs()

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
                    addressErrorMessage = uiState.addressErrorMessage,
                    onValueChange = { event(ConfirmDetailsUiEvent.OnChangeAddress(it)) },
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
                    selectedItem = bikeType,
                    onClickItem = {
                        event(ConfirmDetailsUiEvent.OnChangeBikeType(TextFieldValue(text = it)))
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
                        event(ConfirmDetailsUiEvent.OnChangeDescription(it))
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
                    message = message,
                    onChangeValueMessage = {
                        event(ConfirmDetailsUiEvent.OnChangeMessage(it))
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

                ButtonNavigation(
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


                if (uiState.backgroundLocationPermissionDialogVisible) {
                    DialogBackgroundLocationPermission(
                        modifier = Modifier.constrainAs(permissionDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        }, onDismiss = {
                            event(ConfirmDetailsUiEvent.DismissBackgroundLocationDialog)
                        })
                }

                if (uiState.foregroundLocationPermissionDialogVisible) {
                    DialogForegroundLocationPermission(
                        modifier = Modifier.constrainAs(permissionDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        },
                        onDismiss = {
                            event(ConfirmDetailsUiEvent.DismissForegroundLocationDialog)
                        }
                    )
                }
            }

        }
    }

}


