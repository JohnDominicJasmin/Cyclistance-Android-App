package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueDetailsContent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsUiEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsUiState

@Composable
fun RescueDetailsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RescueDetailsViewModel = hiltViewModel(),

) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var uiState by rememberSaveable { mutableStateOf(RescueDetailsUiState()) }




    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RescueDetailsEvent.GetRescueRecordFailed -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is RescueDetailsEvent.GetRideSummarySuccess -> {
                    uiState = uiState.copy(
                      rideSummary = event.rideSummary
                    )
                }

                is RescueDetailsEvent.GetRideMetricsSuccess -> {
                    uiState = uiState.copy(
                        rideMetrics = event.rideMetrics
                    )
                }
            }
        }
    }

    val closeRescueDetails = remember {
        {
            navController.popBackStack()
        }
    }

    RescueDetailsContent(
        
        modifier = Modifier.padding(paddingValues),
        state = state,
        uiState = uiState,
        event = { event ->
            when (event) {
                RescueDetailsUiEvent.CloseRescueDetails -> closeRescueDetails()
            }
        }
    )
}