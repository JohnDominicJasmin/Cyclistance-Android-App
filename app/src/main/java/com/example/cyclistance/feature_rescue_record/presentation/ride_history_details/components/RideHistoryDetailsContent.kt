package com.example.cyclistance.feature_rescue_record.presentation.ride_history_details.components

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
import com.example.cyclistance.core.presentation.dialogs.common.AnimatedRawResIcon
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.formatter.IconFormatter.rescueDescriptionToIcon
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RatingCard
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueDescription
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueLocationDetails
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.RescueStats
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RideHistoryDetailsContent(
    modifier: Modifier = Modifier,
    rideHistoryDetailsModel: RideHistoryDetailsModel = RideHistoryDetailsModel()) {

    Surface(
        modifier = modifier
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
                    rating = rideHistoryDetailsModel.getRating(),
                    ratingText = rideHistoryDetailsModel.getRatingText(),
                )

                RescueDescription(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    iconDescription = rideHistoryDetailsModel.getTextDescription().rescueDescriptionToIcon(),
                    textDescription = rideHistoryDetailsModel.getTextDescription(),
                    bikeType = rideHistoryDetailsModel.getBikeType()
                )

                RescueLocationDetails(
                    modifier = Modifier.padding(vertical = 12.dp),
                    date = rideHistoryDetailsModel.getDate(),
                    startingTime = rideHistoryDetailsModel.getStartingTime(),
                    endTime = rideHistoryDetailsModel.getEndTime(),
                    startingAddress = rideHistoryDetailsModel.getStartingAddress(),
                    destinationAddress = rideHistoryDetailsModel.getDestinationAddress()
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
                    duration = rideHistoryDetailsModel.getDuration(),
                    distance = rideHistoryDetailsModel.getDistance(),
                    maxSpeed = rideHistoryDetailsModel.getMaxSpeed(),
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
    rescuerId = "1",// parameter
    rescuerName = "John Doe", // parameter
    rescuerPhotoUrl = "aksodnas", // parameter
    rescueeId = "2", // your id
    rescueeName = "Jane Doe", // your name
    rescueePhotoUrl = "asiinaisd", // your photourl
    rescueDetailsModel = RescueDetailsModel(
        rating = 4.5,// to be computed
        ratingText = "Very good", // to be computed
        date = "12/12/2020", // Date()
        textDescription = MappingConstants.INCIDENT_TEXT, // flow
        bikeType = "Mountain Bike", // flow
        startingTime = "12:00", // flow
        endTime = "13:00", // flow
        startingAddress = "Via Roma 1, Milano", // flow and  geocode
        destinationAddress = "Via Roma 2, Milano", // flow and geocode
        duration = "1h 30m", //flow
        distance = "10 km", // flow
        maxSpeed = "30 km/h", // flow
    )

)


@Preview
@Composable
fun PreviewRideHistoryDetailsContentDark() {
    CyclistanceTheme(darkTheme = true) {
        RideHistoryDetailsContent(
            rideHistoryDetailsModel = fakeRideHistoryDetailsModel
        )
    }
}