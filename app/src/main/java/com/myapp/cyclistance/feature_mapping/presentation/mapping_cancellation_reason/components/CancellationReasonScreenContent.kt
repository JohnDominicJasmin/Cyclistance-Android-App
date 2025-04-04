package com.myapp.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.myapp.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.myapp.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.myapp.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonUiState
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun CancellationReasonScreenContent(
    modifier: Modifier = Modifier,
    message: TextFieldValue,
    cancellationType: String = MappingConstants.SELECTION_RESCUEE_TYPE,
    state: CancellationReasonState = CancellationReasonState(),
    uiState: CancellationReasonUiState,
    event: (CancellationReasonUiEvent) -> Unit = {}) {

    Surface(modifier = modifier, color = MaterialTheme.colors.background) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {

            val (radioButtonsSection, additionalMessageSection, buttonNavButtonSection, circularProgressBar, noInternetScreen) = createRefs()

            RadioButtonsSection(
                modifier = Modifier
                    .constrainAs(radioButtonsSection) {
                        top.linkTo(parent.top, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }, cancellationType = cancellationType,
                selectedOption = uiState.selectedReason,
                errorMessage = uiState.selectedReasonErrorMessage,
                onSelectReason = {
                    event(CancellationReasonUiEvent.SelectReason(it))
                })

            AdditionalMessage(
                text = "Additional comments:",
                modifier = Modifier
                    .constrainAs(additionalMessageSection) {
                        top.linkTo(radioButtonsSection.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.percent(0.30f)
                        width = Dimension.percent(0.9f)

                    },
                message = message,
                onChangeValueMessage = {
                    event(CancellationReasonUiEvent.OnChangeMessage(it))
                },
                enabled = state.isLoading.not()
            )


            ButtonNavigation(
                modifier = Modifier
                    .constrainAs(buttonNavButtonSection) {
                        top.linkTo(additionalMessageSection.bottom, margin = 50.dp)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.percent(0.1f)
                        width = Dimension.percent(0.9f)
                    },
                onClickNegativeButton = { event(CancellationReasonUiEvent.NavigateToMapping) },
                onClickPositiveButton = { event(CancellationReasonUiEvent.ConfirmCancellationReason) },
                negativeButtonEnabled = state.isLoading.not(),
                positiveButtonEnabled = state.isLoading.not())


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
                    onDismiss = { event(CancellationReasonUiEvent.DismissNoInternetDialog) },
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

@Preview(name = "CancellationReasonScreen Dark Theme", device = "id:Galaxy Nexus")
@Composable
fun CancellationReasonPreviewDark() {

    CyclistanceTheme(true) {
        CancellationReasonScreenContent(
            modifier = Modifier.fillMaxSize(),
            state = CancellationReasonState(),
            uiState = CancellationReasonUiState(),
            message = TextFieldValue(""),
        )
    }
}

@Preview(name = "CancellationReasonScreen Light Theme", device = "id:Galaxy Nexus")
@Composable
fun CancellationReasonPreviewLight() {

    CyclistanceTheme(false) {
        CancellationReasonScreenContent(
            modifier = Modifier.fillMaxSize(),
            state = CancellationReasonState(),
            uiState = CancellationReasonUiState(),
            message = TextFieldValue(""),
        )
    }
}

