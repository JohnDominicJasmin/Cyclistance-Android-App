package com.example.cyclistance.feature_rescue_record.presentation.history_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.AnimatedRawResIcon
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.formatter.IconFormatter.rescueDescriptionToIcon
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.example.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.history_details.state.HistoryDetailsState
import com.example.cyclistance.feature_rescue_record.presentation.history_details.state.HistoryDetailsUiState
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RatingCard
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueDescription
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueLocationDetails
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueStats
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun HistoryDetailsContent(
    modifier: Modifier = Modifier,
    uiState: HistoryDetailsUiState,
    state: HistoryDetailsState,
    event: (HistoryDetailsUiEvent) -> Unit
) {

    val rescueRide = uiState.rescueRide
    val rideSummary = rescueRide?.rideSummary
    val rideMetrics = rescueRide?.rideMetrics
    val isRideLoaded = rescueRide != null
    val shouldShowPlaceholder = !isRideLoaded && state.isLoading
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = if (shouldShowPlaceholder) Arrangement.Center else Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                item {


                    if (!isRideLoaded) {
                        HistoryDetailsPlaceholder()
                        return@item
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        HistoryDetailsRole(
                            modifier = Modifier
                                .weight(3f)
                                .padding(vertical = 10.dp),
                            role = "Rescuer",
                            photoUrl = rescueRide!!.rescuerPhotoUrl,
                            name = rescueRide.rescuerName,
                        )

                        AnimatedRawResIcon(
                            modifier = Modifier
                                .weight(1.25f)
                                .requiredHeight(100.dp),
                            resId = R.raw.handshake
                        )

                        HistoryDetailsRole(
                            modifier = Modifier
                                .weight(3f)
                                .padding(vertical = 10.dp),
                            role = "Rescuee",
                            photoUrl = rescueRide.rescueePhotoUrl,
                            name = rescueRide.rescueeName,
                        )
                    }

                    RatingCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        rating = rideSummary!!.rating,
                        ratingText = rideSummary.ratingText
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
                        modifier = Modifier.padding(vertical = 12.dp),
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
                            event(HistoryDetailsUiEvent.CloseHistoryDetails)
                        },
                        modifier = Modifier.padding(top = 22.dp, bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp)) {
                        Text(
                            text = "Okay",
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 22.dp))
                    }


                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.primary)
            }
        }

    }


}


val fakeRescueRide = RescueRide(
    rescuerId = "1",
    rescuerName = "John Doe",
    rescuerPhotoUrl = "aksodnas",
    rescueeId = "2",
    rescueeName = "Jane Doe, Jane Doe ,Jane Doe ,Jane Doe ,Jane Doe",
    rescueePhotoUrl = "asiinaisd",
    rideSummary = RideSummary(
        rating = 4.5,
        ratingText = "Very good",
        date = "12/12/2020",
        iconDescription = MappingConstants.FAULTY_BRAKES_TEXT,
        bikeType = "Mountain Bike",
        startingTime = "12:00",
        endTime = "13:00",
        startingAddress = "Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano, Via Roma 1, Milano,Via Roma 1, Milano",
        destinationAddress = "Via Roma 2, Milano,Via Roma 2, Milano,Via Roma 2, Milano,Via Roma 2, Milano,Via Roma 2, Milano",
        duration = "1h 30m",
), rideMetrics = RideMetrics())


@Preview
@Composable
fun PreviewRideHistoryDetailsContentDark() {
    CyclistanceTheme(darkTheme = true) {
        HistoryDetailsContent(
            uiState = HistoryDetailsUiState(
               rescueRide = fakeRescueRide
            ),
            event = {},
            state = HistoryDetailsState(isLoading = true)

        )
    }
}