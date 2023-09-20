package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.BikeType
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultUiEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultState
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueResultsScreenContent(
    modifier: Modifier = Modifier,
    uiState: RescueResultUiState,
    state: RescueResultState,
    event: (RescueResultUiEvent) -> Unit

) {


    val isRated by remember(uiState.step, uiState.rating) {
        derivedStateOf {
            uiState.step == 2 && uiState.rating > 0.0f
        }
    }


    val fadeInAnimationSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = 1200, delayMillis = 250, easing = FastOutSlowInEasing)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
                    .align(alignment = Alignment.TopCenter)
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    alignment = Alignment.Top
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedVisibility(
                    enter = fadeIn(),
                    exit = fadeOut(),
                    visible = uiState.step == 1,
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    RescueArrivedSection(
                        modifier = Modifier.padding(vertical = 22.dp)
                    )
                }


                AnimatedVisibility(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 16.dp),
                    visible = uiState.step == 1,
                    enter = fadeIn(),
                    exit = fadeOut()) {

                    RescueReportAccountSection(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .animateContentSize(),
                        photoUrl = state.rideDetails.rescuerPhotoUrl,
                        name = state.rideDetails.rescuerName,
                        onClickReportAccount = {
                            event(RescueResultUiEvent.ReportAccount(
                                id = state.rideDetails.rescuerId,
                                name = state.rideDetails.rescuerName,
                                photo = state.rideDetails.rescuerPhotoUrl
                            ))
                        }, viewProfile = {
                            event(RescueResultUiEvent.ViewProfile(id = state.rideDetails.rescuerId))
                        })

                }

                AnimatedVisibility(
                    visible = uiState.step == 2,
                    enter = fadeIn(
                        animationSpec = fadeInAnimationSpec
                    ),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)) {

                    RescueGoodToHearSection()
                }


                AnimatedVisibility(
                    visible = uiState.step == 2,
                    enter = fadeIn(animationSpec = fadeInAnimationSpec),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)) {

                    RateRescuer(onValueChange = { event(RescueResultUiEvent.ChangeRating(it)) }, rating = uiState.rating)
                }


                AnimatedVisibility(
                    visible = uiState.step == 3,
                    enter = fadeIn(animationSpec = fadeInAnimationSpec),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)) {

                    RescueThankYouSection()
                }
            }



            AnimatedVisibility(
                visible = uiState.step == 1,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)) {

                ButtonNavigation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    negativeButtonText = "No",
                    positiveButtonText = "Yes",
                    onClickNegativeButton = { event(RescueResultUiEvent.CloseRescueResults) },
                    onClickPositiveButton = {event(RescueResultUiEvent.StepUp)})

            }

            AnimatedVisibility(
                visible = uiState.step == 2,
                enter = fadeIn(animationSpec = fadeInAnimationSpec),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)) {

                ButtonNavigation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    positiveButtonEnabled = isRated,
                    negativeButtonText = "Cancel",
                    positiveButtonText = "Rate",
                    onClickNegativeButton = { event(RescueResultUiEvent.StepDown )},
                    onClickPositiveButton = { event(RescueResultUiEvent.RateRescuer) })

            }


            AnimatedVisibility(
                visible = uiState.step == 3,
                enter = fadeIn(animationSpec = fadeInAnimationSpec),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)
            ) {


                Button(
                    onClick = { event(RescueResultUiEvent.ShowRescueDetails)},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {

                    Text(
                        text = "Okay",
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                }

            }

            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueResultsScreenContentDark() {

    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            RescueResultsScreenContent(event = {}, uiState = RescueResultUiState(rating = 3.8f), state = RescueResultState(
                isLoading = true,
                rideDetails = fakeRideDetails))
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueResultsScreenContentLight() {

    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            RescueResultsScreenContent(event = {}, uiState = RescueResultUiState(rating = 2.0f), state = RescueResultState(
                isLoading = true,
                rideDetails = fakeRideDetails))
        }
    }
}

val fakeRideDetails = RideDetails(
    rescuerId = "10",
    rescuerName = "John Doee",
    rescueePhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
    rescuerPhotoUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
    rescueeId = "312983120",
    rescueeName = "Jane Doe",
    rideSummary = RideSummary(
        rating = 4.4,
        ratingText = "Good",
        iconDescription = "Good ride",
        bikeType = BikeType.MountainBike.type,
        date = "12/12/2020",
        startingAddress = "123 Main St",
        destinationAddress = "456 Main St",
        duration = "1h 30m",
        distance = "10km",
        maxSpeed = "30km/h",
        startingTime = "12:00",
        endTime = "13:30"
    )
)


