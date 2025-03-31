package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.components

import androidx.compose.foundation.ScrollState
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
import com.myapp.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogBackgroundLocationPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogForegroundLocationPermission
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessBackgroundLocationDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessForegroundLocationDialog
import com.myapp.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.myapp.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsUiState
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.CyclistanceTheme




@Composable
fun ConfirmDetailsContent(
    modifier: Modifier,
    state: ConfirmDetailsState = ConfirmDetailsState(),
    bikeType: TextFieldValue,
    message: TextFieldValue,
    address: TextFieldValue,
    uiState: ConfirmDetailsUiState = ConfirmDetailsUiState(),
    scrollState: ScrollState,
    event: (ConfirmDetailsUiEvent) -> Unit = {}) {


    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(scrollState)) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {

                val (addressTextField, bikeTypeDropDownList, buttonDescriptionSection, additionalMessageSection, buttonNavButtonSection, noteText, circularProgressBar, dialog) = createRefs()

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
                    onClickNegativeButton = {
                        event(ConfirmDetailsUiEvent.CancelConfirmation)
                    },
                    onClickPositiveButton = {
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


                if(uiState.prominentBackgroundLocationDialogVisible){
                    AccessBackgroundLocationDialog(
                        onDismissRequest = {
                            event(ConfirmDetailsUiEvent.DismissProminentBackgroundLocationDialog)
                        }, onDeny = {
                            event(ConfirmDetailsUiEvent.DismissProminentBackgroundLocationDialog)
                        }, onAllow = {
                            event(ConfirmDetailsUiEvent.DismissProminentBackgroundLocationDialog)
                            event(ConfirmDetailsUiEvent.AllowProminentBackgroundLocationDialog)
                        }
                    )
                }


                if (uiState.backgroundLocationPermissionDialogVisible) {
                    DialogBackgroundLocationPermission(
                        modifier = Modifier.constrainAs(dialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        }, onDismiss = {
                            event(ConfirmDetailsUiEvent.DismissBackgroundLocationDialog)
                        })
                }

                if (uiState.isNoInternetVisible) {
                    NoInternetDialog(
                        onDismiss = { event(ConfirmDetailsUiEvent.DismissNoInternetDialog) },
                        modifier = Modifier.constrainAs(dialog) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        })

                }

                if (uiState.foregroundLocationPermissionDialogVisible) {
                    DialogForegroundLocationPermission(
                        modifier = Modifier.constrainAs(dialog) {
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

                if(uiState.prominentForegroundLocationDialogVisible){
                    AccessForegroundLocationDialog(onDismissRequest = {
                        event(ConfirmDetailsUiEvent.DismissProminentForegroundLocationDialog)
                    }, onDeny = {
                        event(ConfirmDetailsUiEvent.DismissProminentForegroundLocationDialog)
                    }, onAllow = {
                        event(ConfirmDetailsUiEvent.DismissProminentForegroundLocationDialog)
                        event(ConfirmDetailsUiEvent.AllowProminentForegroundLocationDialog)
                    })
                }



            }

        }
    }

}

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
            scrollState = rememberScrollState(),
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
            uiState = ConfirmDetailsUiState(
                backgroundLocationPermissionDialogVisible = true,
                isNoInternetVisible = true),
            bikeType = TextFieldValue(""),
            message = TextFieldValue(""),
            address = TextFieldValue(""),
            scrollState = rememberScrollState(),
        )
    }
}
