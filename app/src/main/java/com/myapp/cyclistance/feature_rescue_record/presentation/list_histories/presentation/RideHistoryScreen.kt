package com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation

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
import com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.components.RideHistoryContent
import com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.event.RideHistoryEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.event.RideHistoryUiEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.state.RideHistoryUiState
import com.myapp.cyclistance.navigation.Screens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RideHistoryScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: RideHistoryViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable{ mutableStateOf(RideHistoryUiState()) }

    val context = LocalContext.current
    LaunchedEffect(key1 = true,){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is RideHistoryEvent.GetRideHistoryFailed -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is RideHistoryEvent.GetRideHistorySuccess -> {
                    uiState = uiState.copy(
                        rideHistory = event.rideHistory
                    )
                }
            }
        }
    }

    val showRideDetails = remember{{ rideId: String ->
        navController.navigate(Screens.RescueRecordNavigation.RideHistoryDetails.passArgument(transactionId = rideId))
    }}

    RideHistoryContent(
        modifier = Modifier.padding(paddingValues),
        event = { event ->
            when (event) {
                is RideHistoryUiEvent.ShowRideDetails -> showRideDetails(event.rideId)
            }
        },
        uiState = uiState,
        state = state
    )


}