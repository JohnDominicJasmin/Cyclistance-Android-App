package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_dialogs.presentation.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.components.RadioButtonsSection
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonState
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CancellationReasonScreen(
    navController: NavController,
    viewModel: CancellationReasonViewModel = hiltViewModel(),
    cancellationType: String = MappingConstants.SELECTION_RESCUEE_TYPE,
    paddingValues: PaddingValues) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var uiState by rememberSaveable{ mutableStateOf(CancellationReasonUiState()) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CancellationReasonEvent.ConfirmCancellationReasonSuccess -> {
                    navController.navigateScreen(Screens.MappingScreen.route)
                }

                is CancellationReasonEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is CancellationReasonEvent.UserFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is CancellationReasonEvent.RescueTransactionFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is CancellationReasonEvent.NoInternetConnection -> {
                    uiState = uiState.copy(isNoInternetVisible = true)
                }

                is CancellationReasonEvent.InvalidCancellationReason -> {
                    uiState = uiState.copy(selectedReasonErrorMessage = event.reason)
                }
            }
        }
    }


    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(isNoInternetVisible = false)
        }
    }

    val onChangeValueMessage = remember {
        { messageInput: String ->
            uiState = uiState.copy(message = messageInput)
        }
    }
    val onClickConfirmButton = remember {
        {
            viewModel.onEvent(
                event = CancellationReasonVmEvent.ConfirmCancellationReason(
                    reason = uiState.selectedReason,
                    message = uiState.message,
                ))
        }
    }
    val onClickCancelButton = remember {
        {
            navController.navigateScreen(Screens.MappingScreen.route)
        }
    }
    val onSelectReason = remember {
        { reason: String ->
            uiState = uiState.copy(selectedReason = reason, selectedReasonErrorMessage = "")
        }
    }

    CancellationReasonScreenContent(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        cancellationType = cancellationType,
        state = state,
        uiState = uiState,
        event = { event ->
            when(event){
                is CancellationReasonUiEvent.ChangeMessage ->  onChangeValueMessage(event.message)
                is CancellationReasonUiEvent.ConfirmCancellationReason -> onClickConfirmButton()
                is CancellationReasonUiEvent.NavigateToMapping ->  onClickCancelButton()
                is CancellationReasonUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is CancellationReasonUiEvent.SelectReason -> onSelectReason(event.reason)
            }
        }
    )

}

@Composable
fun CancellationReasonScreenContent(
    modifier: Modifier = Modifier,
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
                message = uiState.message,
                onChangeValueMessage = {
                    event(CancellationReasonUiEvent.ChangeMessage(it))
                },
                enabled = state.isLoading.not()
            )


            MappingButtonNavigation(
                modifier = Modifier
                    .constrainAs(buttonNavButtonSection) {
                        top.linkTo(additionalMessageSection.bottom, margin = 50.dp)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.percent(0.1f)
                        width = Dimension.percent(0.9f)
                    },
                onClickCancelButton = { event(CancellationReasonUiEvent.NavigateToMapping) },
                onClickConfirmButton = { event(CancellationReasonUiEvent.ConfirmCancellationReason) },
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

@Preview
@Composable
fun CancellationReasonPreview() {

    CyclistanceTheme(true) {
        CancellationReasonScreenContent(
            modifier = Modifier.fillMaxSize(),
            state = CancellationReasonState(),
            uiState = CancellationReasonUiState()
        )
    }
}

