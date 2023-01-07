package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.components.RadioButtonsSection
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CancellationReasonScreen(
    hasInternetConnection: Boolean,
    navController: NavController,
    viewModel: CancellationReasonViewModel = hiltViewModel(),
    cancellationType: String = MappingConstants.SELECTION_RESCUEE_TYPE,
    paddingValues: PaddingValues) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
           when(event){
               is CancellationReasonUiEvent.ShowMappingScreen -> {
                   navController.navigateScreen(Screens.MappingScreen.route)
               }
               is CancellationReasonUiEvent.ShowToastMessage -> {
                   Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
               }

           }
        }
    }

    val onDismissNoInternetDialog = remember{{
        viewModel.onEvent(event = CancellationReasonEvent.DismissNoInternetDialog)
    }}

    val onChangeValueMessage = remember{{ message: String ->
        viewModel.onEvent(event = CancellationReasonEvent.EnterMessage(message))
    }}
    val onClickConfirmButton = remember{{
        viewModel.onEvent(event = CancellationReasonEvent.ConfirmCancellationReason)
    }}
    val onClickCancelButton = remember{{
        navController.navigateScreen(Screens.MappingScreen.route)
    }}
    val onSelectReason = remember{{ reason: String ->
        viewModel.onEvent(event = CancellationReasonEvent.SelectReason(reason))
        viewModel.onEvent(event = CancellationReasonEvent.ClearReasonErrorMessage)
    }}

    CancellationReasonScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        cancellationType = cancellationType,
        state = state,
        onChangeValueMessage = onChangeValueMessage,
        onClickConfirmButton = onClickConfirmButton,
        onClickCancelButton = onClickCancelButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        onSelectReason = onSelectReason
    )

}

@Composable
fun CancellationReasonScreenContent(
    modifier: Modifier = Modifier,
    cancellationType: String = MappingConstants.SELECTION_RESCUEE_TYPE,
    state: CancellationReasonState = CancellationReasonState(),
    onChangeValueMessage: (String) -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
    onClickCancelButton: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {},
    onSelectReason: (String) -> Unit = {}){

    ConstraintLayout(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {

        val (radioButtonsSection, additionalMessageSection, buttonNavButtonSection, circularProgressBar, noInternetScreen) = createRefs()

        RadioButtonsSection(modifier = Modifier
            .constrainAs(radioButtonsSection) {
                top.linkTo(parent.top, margin = 15.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }, cancellationType = cancellationType,
            selectedOption = state.selectedReason,
            errorMessage = state.reasonErrorMessage,
            onSelectReason = onSelectReason)

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
            message = state.message,
            onChangeValueMessage = onChangeValueMessage,
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
            onClickCancelButton = onClickCancelButton,
            onClickConfirmButton = onClickConfirmButton,
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
        if (!state.hasInternet) {
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

@Preview
@Composable
fun CancellationReasonPreview() {

    CyclistanceTheme(true) {
        CancellationReasonScreenContent(modifier = Modifier .fillMaxSize(), state = CancellationReasonState(reasonErrorMessage = "Sample Error MEssage"))
    }
}

