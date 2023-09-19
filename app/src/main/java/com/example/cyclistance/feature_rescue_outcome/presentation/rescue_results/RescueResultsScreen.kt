package com.example.cyclistance.feature_rescue_outcome.presentation.rescue_results

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_rescue_outcome.presentation.rescue_results.components.RescueResultsScreenContent
import com.example.cyclistance.feature_rescue_outcome.presentation.rescue_results.event.RescueResultUiEvent

@Composable
fun RescueResultsScreen(
    paddingValues: PaddingValues,
    navController: NavController

) {


    RescueResultsScreenContent(modifier = Modifier.padding(paddingValues), event = { event ->
        when (event) {
            RescueResultUiEvent.CloseRescueResults -> navController.popBackStack()
        }

    })


}