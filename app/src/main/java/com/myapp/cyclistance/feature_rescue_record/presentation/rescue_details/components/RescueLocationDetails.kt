package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
internal fun RescueLocationDetails(
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
                    .fillMaxHeight()
                    .weight(0.20f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = startingTime,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center)

                Text(
                    text = endTime,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center)
            }

            PointToPointDisplay(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp).weight(0.12f))


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = startingAddress,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = destinationAddress,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )


            }

        }
    }
}


@Preview
@Composable
fun PreviewRescueLocationDetails() {
    CyclistanceTheme(darkTheme  = true){
        RescueLocationDetails(
            modifier = Modifier,
            date = "Date sample",
            startingTime = "Starting time",
            endTime = "End time",
            startingAddress = "aoisd",
            destinationAddress = "oiiiiwow"
        )
    }
}