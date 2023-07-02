package com.example.cyclistance.feature_ride_history.presentation.ride_history_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_ride_history.presentation.ride_history_details.components.RideHistoryDetailsContent
import com.example.cyclistance.feature_ride_history.presentation.ride_history_details.components.fakeRideHistoryDetailsModel

@Composable
fun RideHistoryDetailsScreen(navController: NavController, paddingValues: PaddingValues) {

    RideHistoryDetailsContent(
        modifier = Modifier.padding(paddingValues),
        rideHistoryDetailsModel = fakeRideHistoryDetailsModel
    )
}