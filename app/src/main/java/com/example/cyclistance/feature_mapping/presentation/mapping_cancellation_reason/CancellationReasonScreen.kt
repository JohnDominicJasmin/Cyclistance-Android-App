package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.components.CancellationReasonScreenContent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
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
                    navController.navigateScreen(Screens.Mapping.MappingScreen.screenRoute)
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
        { messageInput: TextFieldValue ->
            uiState = uiState.copy(message = messageInput)
        }
    }
    val onClickConfirmButton = remember {
        {
            viewModel.onEvent(
                event = CancellationReasonVmEvent.ConfirmCancellationReason(
                    reason = uiState.selectedReason,
                    message = uiState.message.text,
                ))
        }
    }
    val onClickCancelButton = remember {
        {
            navController.navigateScreen(Screens.Mapping.MappingScreen.screenRoute)
        }
    }
    val onSelectReason = remember {
        { reason: String ->
            uiState = uiState.copy(selectedReason = reason, selectedReasonErrorMessage = "")
        }
    }

    CancellationReasonScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        cancellationType = cancellationType,
        state = state,
        uiState = uiState,
        event = { event ->
            when(event){
                is CancellationReasonUiEvent.OnChangeMessage -> onChangeValueMessage(event.message)
                is CancellationReasonUiEvent.ConfirmCancellationReason -> onClickConfirmButton()
                is CancellationReasonUiEvent.NavigateToMapping ->  onClickCancelButton()
                is CancellationReasonUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is CancellationReasonUiEvent.SelectReason -> onSelectReason(event.reason)
            }
        }
    )

}
