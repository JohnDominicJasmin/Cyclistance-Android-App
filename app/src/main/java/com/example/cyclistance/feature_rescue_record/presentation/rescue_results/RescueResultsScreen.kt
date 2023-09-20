package com.example.cyclistance.feature_rescue_record.presentation.rescue_results

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.components.RescueResultsScreenContent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen

@Composable
fun RescueResultsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RescueResultViewModel = hiltViewModel()

) {

    var uiState by rememberSaveable{ mutableStateOf(RescueResultUiState()) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val changeRating = remember{{ rate: Float ->
        uiState = uiState.copy(rating = rate)
    }}

    val stepUp = remember{{
        uiState = uiState.copy(
            step = uiState.step + 1
        )
    }}

    val stepDown = remember{{
        uiState = uiState.copy(
            step = uiState.step - 1
        )
    }}

    val viewProfile = remember{{ userId : String ->
        navController.navigateScreen(route = Screens.UserProfileNavigation.UserProfile.passArgument(userId = userId))
    }}

    RescueResultsScreenContent(
        uiState = uiState,
        state = state,
        modifier = Modifier.padding(paddingValues),
        event = { event ->
            when (event) {
                RescueResultUiEvent.CloseRescueResults -> navController.popBackStack()
                is RescueResultUiEvent.ChangeRating -> changeRating(event.rating)
                RescueResultUiEvent.StepDown -> stepDown()
                RescueResultUiEvent.StepUp -> stepUp()
                is RescueResultUiEvent.ReportAccount -> {}
                is RescueResultUiEvent.ViewProfile -> viewProfile(event.id)
                is RescueResultUiEvent.RateRescuer -> rateRescuer()
            }
        })


}