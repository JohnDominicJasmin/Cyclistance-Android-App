package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.BikeType
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details.components.RatingBar
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueDetailsScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

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
                    rating = 4.0, ratingText = "(4.0 • Great)",
                )

                RescueDescription(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    iconDescription = R.drawable.ic_broken_chain,
                    textDescription = "Broken Chain",
                    bikeType = BikeType.MountainBike
                )

                RescueLocationDetails(
                    modifier = Modifier.padding(vertical = 12.dp),
                    date = "Wed, 31 August 2022",
                    startingTime = "10:00 AM",
                    endTime = "11: 00 AM",
                    startingAddress = "Tanauan City Batangas",
                    destinationAddress = "Malvar Batangas"
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
                    duration = "1 hour",
                    distance = "6 km",
                    maxSpeed = "20 km/h"
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

@Composable
private fun RescueStats(
    modifier: Modifier = Modifier,
    duration: String,
    distance: String,
    maxSpeed: String,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "Rescue Duration",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)

            Text(
                text = duration,
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Rescue Distance",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)
            Text(
                text = distance,
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Rescuer Max Speed",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)
            Text(
                text = maxSpeed,
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2)
        }

    }

}

@Composable
private fun RescueLocationDetails(
    modifier: Modifier = Modifier,
    date: String,
    startingTime: String,
    endTime: String,
    startingAddress: String,
    destinationAddress: String) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(
            style = MaterialTheme.typography.subtitle1,
            text = date,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.onBackground)

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = startingTime,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2)
                Text(
                    text = endTime,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2)
            }

            PointToPointDisplay(modifier = Modifier.fillMaxHeight())


            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = startingAddress,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Clip,
                    maxLines = 4,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = destinationAddress,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Clip,
                    maxLines = 4,
                )


            }

        }
    }
}

@Composable
private fun PointToPointDisplay(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Icon(
            modifier = Modifier
                .weight(0.3f, fill = false)
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_starting_destination),
            contentDescription = "Starting Address",
            tint = Color.Unspecified)

        Box(
            modifier = Modifier
                .width(width = 1.dp)
                .fillMaxHeight()
                .background(color = Black500)
                .weight(0.4f, fill = true)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_destination_location),
            contentDescription = "Starting Address",
            tint = Color.Unspecified,
            modifier = Modifier
                .weight(0.3f, fill = false)
                .size(24.dp))

    }
}

@Preview
@Composable
fun PreviewPointToPointDIsplay() {
    CyclistanceTheme(darkTheme = true) {
        PointToPointDisplay(modifier = Modifier.requiredHeight(100.dp))
    }
}

@Composable
private fun RescueDescription(
    modifier: Modifier = Modifier,
    iconDescription: Int,
    textDescription: String,
    bikeType: BikeType) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(
            width = 1.dp, color = Black500
        )) {

        Row(
            modifier = Modifier

                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.5f)
            ) {

                Icon(
                    painter = painterResource(id = iconDescription),
                    contentDescription = "Description Icon",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(0.1f, fill = false)
                )
                Text(
                    text = textDescription,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle2)
            }

            Divider(
                color = MaterialTheme.colors.onSurface, modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.5f)
            ) {

                Icon(
                    imageVector = Icons.Default.PedalBike,
                    contentDescription = "Bike Icon",
                    tint = Black500,
                    modifier = Modifier.weight(0.1f, fill = false)
                )

                Text(
                    text = bikeType.type,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle2)
            }

        }

    }

}

@Composable
fun RatingCard(modifier: Modifier = Modifier, rating: Double, ratingText: String) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier, shape = RoundedCornerShape(8.dp)) {

        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "Your Rating",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                RatingBar(rating = rating)

                Text(
                    text = ratingText,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsDark() {
    CyclistanceTheme(darkTheme = true) {
        RescueDetailsScreen()
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsLight() {
    CyclistanceTheme(darkTheme = false) {
        RescueDetailsScreen()
    }
}

