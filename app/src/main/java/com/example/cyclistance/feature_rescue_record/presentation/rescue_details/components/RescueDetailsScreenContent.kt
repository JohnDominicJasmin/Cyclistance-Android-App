package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import com.example.cyclistance.core.utils.formatter.IconFormatter.rescueDescriptionToIcon
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: RescueDetailsState,
    event: (RescueDetailsUiEvent) -> Unit
) {


    val rideSummary = state.rideSummary

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (rideSummary != null) {
                item {
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
                        distance = rideSummary.distance,
                        maxSpeed = rideSummary.maxSpeed
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

                }

            }

        }

    }
}

val fakeRideSummary = RideSummary(
    rating = 4.5,
    ratingText = "Very good",
    iconDescription = "Injury",
    bikeType = "Mountain Bike",
    date = "12/12/2020",
    startingTime = "12:00",
    endTime = "13:00",
    startingAddress = "Via Roma 1, Milano",
    destinationAddress = "Via Roma 2, Milano",
    duration = "1h 30m",
    distance = "10 km",
    maxSpeed = "30 km/h",
)


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsDark() {
    CyclistanceTheme(darkTheme = true) {
        RescueDetailsScreenContent(
            state = RescueDetailsState(
                rideSummary = fakeRideSummary), event = {})
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsLight() {
    CyclistanceTheme(darkTheme = false) {
        RescueDetailsScreenContent(
            state = RescueDetailsState(rideSummary = fakeRideSummary),
            event = {})
    }
}

