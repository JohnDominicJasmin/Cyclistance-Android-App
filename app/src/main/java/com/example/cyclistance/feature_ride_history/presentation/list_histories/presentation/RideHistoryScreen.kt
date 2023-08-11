package com.example.cyclistance.feature_ride_history.presentation.list_histories.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_ride_history.presentation.list_histories.presentation.components.RideHistoryContent
import com.example.cyclistance.feature_ride_history.presentation.list_histories.presentation.components.fakeHistory
import com.example.cyclistance.navigation.Screens

@Composable
fun RideHistoryScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {

    RideHistoryContent(
        rideHistory = fakeHistory,
        modifier = Modifier.padding(paddingValues), onClick = {
            navController.navigate(Screens.RideHistoryNavigation.RideHistoryDetails.screenRoute)
        })


}