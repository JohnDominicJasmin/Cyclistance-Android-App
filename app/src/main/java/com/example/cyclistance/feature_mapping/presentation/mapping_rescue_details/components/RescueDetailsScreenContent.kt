package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details.components

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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils.bikeDescriptionToIcon
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue_details.RescueDetailsModel
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator

@Composable
fun RescueDetailsScreenContent(
    modifier: Modifier = Modifier,
    rescueDetailsModel: RescueDetailsModel = RescueDetailsModel()) {

    Scaffold(modifier = modifier, topBar = {
        TopAppBarCreator(
            icon = Icons.Default.Close,
            onClickIcon = { },
            topAppBarTitle = {
                TitleTopAppBar(title = "Rescue Request")
            })

    }) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colors.background) {
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
                        rating = rescueDetailsModel.rating,
                        ratingText = rescueDetailsModel.ratingText,
                    )

                    RescueDescription(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        iconDescription = rescueDetailsModel.textDescription.bikeDescriptionToIcon(),
                        textDescription = rescueDetailsModel.textDescription,
                        bikeType = rescueDetailsModel.bikeType
                    )

                    RescueLocationDetails(
                        modifier = Modifier.padding(vertical = 12.dp),
                        date = rescueDetailsModel.date,
                        startingTime = rescueDetailsModel.startingTime,
                        endTime = rescueDetailsModel.endTime,
                        startingAddress = rescueDetailsModel.startingAddress,
                        destinationAddress = rescueDetailsModel.destinationAddress
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
                        duration = rescueDetailsModel.duration,
                        distance = rescueDetailsModel.distance,
                        maxSpeed = rescueDetailsModel.maxSpeed
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
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsDark() {
    CyclistanceTheme(darkTheme = true) {
        RescueDetailsScreenContent(
            rescueDetailsModel = RescueDetailsModel(
                rating = 4.5,
                ratingText = "Very good",
                textDescription = "Injury",
                bikeType = "Mountain Bike",
                date = "12/12/2020",
                startingTime = "12:00",
                endTime = "13:00",
                startingAddress = "Via Roma 1, Milano",
                destinationAddress = "Via Roma 2, Milano",
                duration = "1h 30m",
                distance = "10 km",
                maxSpeed = "30 km/h",
            ))
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueDetailsLight() {
    CyclistanceTheme(darkTheme = false) {
        RescueDetailsScreenContent(
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

                ),

            )
    }
}

