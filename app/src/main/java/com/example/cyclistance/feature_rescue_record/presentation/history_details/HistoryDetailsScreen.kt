package com.example.cyclistance.feature_rescue_record.presentation.history_details

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
import com.example.cyclistance.feature_rescue_record.presentation.history_details.components.HistoryDetailsContent
import com.example.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsEvent
import com.example.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.history_details.state.HistoryDetailsUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HistoryDetailsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: HistoryDetailsViewModel = hiltViewModel(),
    transactionId: String
    ) {

    val context = LocalContext.current
    var uiState by rememberSaveable { mutableStateOf(HistoryDetailsUiState()) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val closeHistoryDetails =  remember{{
        navController.popBackStack()
    }}
    LaunchedEffect(key1 = transactionId){
        viewModel.onEvent(event = HistoryDetailsVmEvent.LoadRideDetails(transactionId = transactionId))
    }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is HistoryDetailsEvent.GetRideDetailsSuccess -> {
                    uiState = uiState.copy(
                        rescueRide = event.rescueRide
                    )
                }
                is HistoryDetailsEvent.GetRideDetailsFailed -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    HistoryDetailsContent(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        state = state,
        event = {event ->
            when(event){
                HistoryDetailsUiEvent.CloseHistoryDetails -> closeHistoryDetails()
            }

        }
    )
}