package com.example.cyclistance.feature_rescue_record.presentation.rescue_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueDetailsScreenContent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsVmEvent

@Composable
fun RescueDetailsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RescueDetailsViewModel = hiltViewModel(),
    transactionId: String
) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = transactionId){
        viewModel.onEvent(event = RescueDetailsVmEvent.LoadRescueDetails(transactionId = transactionId))
    }

    val closeRescueDetails = remember {{
        navController.popBackStack()
    }}

    RescueDetailsScreenContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        event = { event ->
            when(event) {
                RescueDetailsUiEvent.CloseRescueDetails -> closeRescueDetails()
            }
        }
    )
}