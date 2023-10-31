package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.formatter.IconFormatter.rescueDescriptionToIcon
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.example.cyclistance.feature_rescue_record.presentation.history_details.components.HistoryDetailsPlaceholder
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsState
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsUiState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueDetailsContent(
    modifier: Modifier = Modifier,
    state: RescueDetailsState,
    uiState: RescueDetailsUiState,
    event: (RescueDetailsUiEvent) -> Unit
) {


    val rescueRide = uiState.rescueRide
    val rideSummary = rescueRide?.rideSummary
    val rideMetrics = rescueRide?.rideMetrics

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {


                    item {
                        if (rideSummary != null) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rescue_details_like),
                            contentDescription = "Like Image",
                            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp))

                        Text(
                            text = "Thank you for your assistance!",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.subtitle2.copy(fontSize = MaterialTheme.typography.subtitle1.fontSize),
                            modifier = Modifier.padding(all = 8.dp)
                        )


                        RatingCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            rating = rideSummary.rating,
                            ratingText = rideSummary.ratingText,
                        )

                        RescueDescription(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            iconDescription = rideSummary.iconDescription.rescueDescriptionToIcon(),
                            textDescription = rideSummary.iconDescription,
                            bikeType = rideSummary.bikeType
                        )

                        RescueLocationDetails(
                            modifier = Modifier,
                            date = rideSummary.date,
                            startingTime = rideSummary.startingTime,
                            endTime = rideSummary.endTime,
                            startingAddress = rideSummary.startingAddress,
                            destinationAddress = rideSummary.destinationAddress
                        )



                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            color = Black500,
                            thickness = 1.5.dp
                        )

                        RescueStats(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            duration = rideSummary.duration,
                            distance = rideMetrics?.distanceInMeters?.formatToDistanceKm() ?: "0.0m",
                            maxSpeed = rideMetrics?.maxSpeed ?: "0.0km/h"
                        )

                        Button(
                            onClick = {
                                event(RescueDetailsUiEvent.CloseRescueDetails)
                            },
                            modifier = Modifier.padding(top = 22.dp, bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp)) {
                            Text(
                                text = "Okay",
                                modifier = Modifier.padding(vertical = 2.dp, horizontal = 22.dp))
                        }

                        } else {
                            HistoryDetailsPlaceholder()
                        }

                }

            }
            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
val fakeRideSummary = RideSummary(
    rating = 0.0,
    ratingText = "No ratings",
    iconDescription = "Injury",
    bikeType = "Mountain Bike",
    date = "12/12/2020",
    startingTime = "12:00",
    endTime = "13:00",
    startingAddress = "Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano,Via Roma 1, Milano",
    destinationAddress = "Via Roma 2, Milano, Via Roma 2, Milano, Via Roma 2, Milano, Via Roma 2, Milano",
    duration = "1h 30m",

    )

val fakeRideMetrics = RideMetrics(
    distanceInMeters = 2352.2,
    maxSpeed = "20.0km/h",
    averageSpeedKmh = 10.0
)

val fakeRescueRide = RescueRide(
    rideSummary = fakeRideSummary,
    rideMetrics = fakeRideMetrics
)



@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsDark() {
    CyclistanceTheme(darkTheme = true) {
        RescueDetailsContent(
            state = RescueDetailsState(isLoading = true),
            uiState = RescueDetailsUiState(rescueRide = fakeRescueRide),
            event = {})
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsLight() {
    CyclistanceTheme(darkTheme = false) {
        RescueDetailsContent(
            state = RescueDetailsState(isLoading = true),
            uiState = RescueDetailsUiState(rescueRide = fakeRescueRide),
            event = {})
    }
}

