package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.components.RescueRequestScreenContent
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.event.RescueRequestUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.state.RescueRequestUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RescueRequestScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mappingViewModel: MappingViewModel) {


    val mappingState by mappingViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var uiState by rememberSaveable { mutableStateOf(RescueRequestUiState()) }

    LaunchedEffect(key1 = true) {
        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is MappingEvent.AcceptRescueRequestSuccess -> {
                    navController.navigateScreen(
                        destination = Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE=${BottomSheetType.OnGoingRescue}")
                }

                is MappingEvent.LocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RescuerLocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetAvailable = true
                    )
                }


                is MappingEvent.RescueHasTransaction -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Cannot Request",
                            description = "Unfortunately the Rescuer is currently in a Rescue.",
                            icon = R.raw.error
                        )
                    )
                }

                is MappingEvent.UserHasCurrentTransaction -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Cannot Request",
                            description = "You can only have one transaction at a time",
                            icon = R.raw.error
                        )
                    )
                }



                else -> {}
            }
        }
    }

    val onClickCancelButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingVmEvent.DeclineRescueRequest(id))
        }
    }

    val onClickConfirmButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingVmEvent.AcceptRescueRequest(id))
        }
    }

    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetAvailable = false
            )
        }
    }

    RescueRequestScreenContent(
        modifier = Modifier
            .padding(paddingValues = paddingValues),
        mappingState = mappingState,
        uiState = uiState,
        event = { event ->
            when(event){
                is RescueRequestUiEvent.CancelRequestHelp -> onClickCancelButton(event.id)
                is RescueRequestUiEvent.ConfirmRequestHelp -> onClickConfirmButton(event.id)
                is RescueRequestUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is RescueRequestUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
            }
        }

    )
}





