package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
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


    LaunchedEffect(key1 = true) {
        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is MappingUiEvent.AcceptRescueRequestSuccess -> {
                    navController.navigateScreen(
                        destination = Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE=${BottomSheetType.OnGoingRescue.type}")
                }

                is MappingUiEvent.RespondToHelpFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.TrackingLocation -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.RescuerLocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.RemovingRespondentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.UserFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingUiEvent.RescueTransactionFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    val onClickCancelButton = remember {{ id: String ->
        mappingViewModel.onEvent(MappingEvent.DeclineRescueRequest(id))
    }}

    val onClickConfirmButton = remember{{ id: String ->
        mappingViewModel.onEvent(MappingEvent.AcceptRescueRequest(id))
    }}

    val onDismissAlertDialog = remember{{
        mappingViewModel.onEvent(MappingEvent.DismissAlertDialog)
    }}
    val onDismissNoInternetDialog = remember{{ ->
        mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetDialog)
    }}

    RescueRequestScreenContent(
        modifier = Modifier
            .padding(paddingValues = paddingValues),
        mappingState = mappingState,
        onClickCancelButton = onClickCancelButton,
        onClickConfirmButton = onClickConfirmButton,
        onDismissAlertDialog = onDismissAlertDialog,
        onDismissNoInternetDialog = onDismissNoInternetDialog
    )
}





