package com.example.cyclistance.feature_rescue_record.presentation.rescue_results

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
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.components.RescueResultsScreenContent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import com.example.cyclistance.navigation.nav_graph.navigateScreenInclusively

@Composable
fun RescueResultsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RescueResultViewModel = hiltViewModel()

) {

    val context = LocalContext.current
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

    val rideDetails = state.rideDetails

    val viewProfile = remember{{ userId : String ->
        navController.navigateScreen(route = Screens.UserProfileNavigation.UserProfile.passArgument(userId = userId))
    }}

    val reportAccount = remember {{ id: String, name: String, photo: String ->
        navController.navigateScreen(
            route = Screens.ReportAccountNavigation.ReportAccount.passArgument(
                userId = id,
                name = name,
                userPhoto = photo))

    }}

    val rateRescue = remember{{
        viewModel.onEvent(event = RescueResultVmEvent.RateRescue(
            rating = uiState.rating
        ))
        viewModel.onEvent(event = RescueResultVmEvent.UpdateUserStats)
    }}

    val showRescueDetails = remember(rideDetails.rideId) {{
        navController.navigateScreenInclusively(destination = Screens.RescueRecordNavigation.RescueDetails.passArgument(transactionId = rideDetails.rideId), popUpToDestination = Screens.RescueRecordNavigation.ROUTE)
    }}


    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collect{ event ->
            when(event){
                is RescueResultEvent.RatingFailed -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                RescueResultEvent.RatingSuccess -> stepUp()
            }
        }
    }

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
                is RescueResultUiEvent.ReportAccount -> reportAccount(event.id, event.name, event.photo)
                is RescueResultUiEvent.ViewProfile -> viewProfile(event.id)
                is RescueResultUiEvent.RateRescuer -> rateRescue()
                RescueResultUiEvent.ShowRescueDetails -> showRescueDetails()
            }
        })


}