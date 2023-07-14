package com.example.cyclistance.feature_ride_history.presentation.ride_history_details.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils.bikeDescriptionToIcon
import com.example.cyclistance.feature_dialogs.presentation.common.AnimatedRawResIcon
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue_details.RescueDetailsModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_details.components.RatingCard
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_details.components.RescueDescription
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_details.components.RescueLocationDetails
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_details.components.RescueStats
import com.example.cyclistance.feature_ride_history.domain.model.ui.RideHistoryDetailsModel
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RideHistoryDetailsContent(
    modifier: Modifier = Modifier,
    rideHistoryDetailsModel: RideHistoryDetailsModel = RideHistoryDetailsModel()) {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    RideHistoryDetailsRole(
                        modifier = Modifier
                            .weight(3f)
                            .padding(vertical = 10.dp),
                        role = "Rescuer",
                        photoUrl = rideHistoryDetailsModel.rescuerPhotoUrl,
                        name = rideHistoryDetailsModel.rescuerName,
                    )

                    AnimatedRawResIcon(
                        modifier = Modifier
                            .weight(1.25f)
                            .requiredHeight(100.dp),
                        resId = R.raw.handshake
                    )

                    RideHistoryDetailsRole(
                        modifier = Modifier
                            .weight(3f)
                            .padding(vertical = 10.dp),
                        role = "Rescuee",
                        photoUrl = rideHistoryDetailsModel.rescueePhotoUrl,
                        name = rideHistoryDetailsModel.rescueeName,
                    )
                }

                RatingCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    rating = rideHistoryDetailsModel.rescueDetailsModel.rating,
                    ratingText = rideHistoryDetailsModel.rescueDetailsModel.ratingText,
                )

                RescueDescription(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    iconDescription = rideHistoryDetailsModel.rescueDetailsModel.textDescription.bikeDescriptionToIcon(),
                    textDescription = rideHistoryDetailsModel.rescueDetailsModel.textDescription,
                    bikeType = rideHistoryDetailsModel.rescueDetailsModel.bikeType
                )

                RescueLocationDetails(
                    modifier = Modifier.padding(vertical = 12.dp),
                    date = rideHistoryDetailsModel.rescueDetailsModel.date,
                    startingTime = rideHistoryDetailsModel.rescueDetailsModel.startingTime,
                    endTime = rideHistoryDetailsModel.rescueDetailsModel.endTime,
                    startingAddress = rideHistoryDetailsModel.rescueDetailsModel.startingAddress,
                    destinationAddress = rideHistoryDetailsModel.rescueDetailsModel.destinationAddress
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
                    duration = rideHistoryDetailsModel.rescueDetailsModel.duration,
                    distance = rideHistoryDetailsModel.rescueDetailsModel.distance,
                    maxSpeed = rideHistoryDetailsModel.rescueDetailsModel.maxSpeed,
                )

                Button(
                    onClick = {},
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

val fakeRideHistoryDetailsModel = RideHistoryDetailsModel(
    rescuerId = "1",
    rescuerName = "John Doe",
    rescuerPhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
    rescueeId = "2",
    rescueeName = "Jane Doe",
    rescueePhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
    rescueDetailsModel = RescueDetailsModel(
        rating = 4.5,
        ratingText = "Very good",
        textDescription = MappingConstants.INJURY_TEXT,
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

)


@Preview
@Composable
fun PreviewRideHistoryDetailsContentDark() {
    CyclistanceTheme(darkTheme = true) {
        RideHistoryDetailsContent(
        )
    }
}