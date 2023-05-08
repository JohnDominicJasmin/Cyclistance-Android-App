package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.components.RescueRequestScreenContent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RescueRequestScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mappingViewModel: MappingViewModel) {


    val mappingState by mappingViewModel.state.collectAsState()
    val context = LocalContext.current
    var isNoInternetDialogVisible by rememberSaveable { mutableStateOf(false) }
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }

    LaunchedEffect(key1 = true) {
        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is MappingUiEvent.AcceptRescueRequestSuccess -> {
                    navController.navigateScreen(
                        destination = Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE=${BottomSheetType.OnGoingRescue}")
                }

                is MappingUiEvent.LocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.RescuerLocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.NoInternetConnection -> {
                    isNoInternetDialogVisible = true
                }


                is MappingUiEvent.RescueHasTransaction -> {
                    alertDialogState = AlertDialogState(
                        title = "Cannot Request",
                        description = "Unfortunately the Rescuer is currently in a Rescue.",
                        icon = R.raw.error
                    )
                }

                is MappingUiEvent.UserHasCurrentTransaction -> {

                    alertDialogState = AlertDialogState(
                        title = "Cannot Request",
                        description = "You can only have one transaction at a time",
                        icon = R.raw.error
                    )
                }



                else -> {}
            }
        }
    }

    val onClickCancelButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingEvent.DeclineRescueRequest(id))
        }
    }

    val onClickConfirmButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingEvent.AcceptRescueRequest(id))
        }
    }

    val onDismissAlertDialog = remember {
        {
            alertDialogState = AlertDialogState()
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            isNoInternetDialogVisible = false
        }
    }

    RescueRequestScreenContent(
        modifier = Modifier
            .padding(paddingValues = paddingValues),
        mappingState = mappingState,
        onClickCancelButton = onClickCancelButton,
        onClickConfirmButton = onClickConfirmButton,
        onDismissAlertDialog = onDismissAlertDialog,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        alertDialogState = alertDialogState,
        isNoInternetDialogVisible = isNoInternetDialogVisible
    )
}





