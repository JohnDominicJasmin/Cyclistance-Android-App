package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_results.components

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_results.event.RescueResultUiEvent
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueResultsScreenContent(
    modifier: Modifier = Modifier,
    event: (RescueResultUiEvent) -> Unit

) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var rating by rememberSaveable { mutableFloatStateOf(0.0f) }

    val isRated by remember(step, rating) {
        derivedStateOf {
            step == 2 && rating > 0.0f
        }
    }


    val stepUp = remember(step) {
        {
            step += 1
        }
    }

    val stepDown = remember(step) {
        {
            step -= 1
        }
    }
    val fadeInAnimationSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = 1200, delayMillis = 250, easing = FastOutSlowInEasing)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
    ) {

        Box {

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
                    visible = step == 1,
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
                    visible = step == 1,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    RescueReportAccountSection(
                        photoUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                        name = "John Doe",
                        onClickReportAccount = {
                            //todo
                        }, modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .animateContentSize()
                    )

                }

                AnimatedVisibility(
                    visible = step == 2,
                    enter = fadeIn(
                        animationSpec = fadeInAnimationSpec
                    ),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)
                ) {
                    RescueGoodToHearSection()
                }


                AnimatedVisibility(
                    visible = step == 2,
                    enter = fadeIn(animationSpec = fadeInAnimationSpec),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)
                ) {
                    RateRescuer(onValueChange = { rating = it }, rating = rating)
                }


                AnimatedVisibility(
                    visible = step == 3,
                    enter = fadeIn(animationSpec = fadeInAnimationSpec),
                    exit = fadeOut(),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 8.dp)
                ) {
                    RescueThankYouSection()
                }

            }



            AnimatedVisibility(
                visible = step == 1,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)
            ) {

                ButtonNavigation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    negativeButtonText = "No",
                    positiveButtonText = "Yes",
                    onClickNegativeButton = { event(RescueResultUiEvent.CloseRescueResults) },
                    onClickPositiveButton = stepUp)

            }

            AnimatedVisibility(
                visible = step == 2,
                enter = fadeIn(animationSpec = fadeInAnimationSpec),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)
            ) {

                ButtonNavigation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    positiveButtonEnabled = isRated,
                    negativeButtonText = "Cancel",
                    positiveButtonText = "Rate",
                    onClickNegativeButton = stepDown,
                    onClickPositiveButton = stepUp)

            }


            AnimatedVisibility(
                visible = step == 3,
                enter = fadeIn(animationSpec = fadeInAnimationSpec),
                exit = fadeOut(),
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 30.dp)
                    .align(alignment = Alignment.BottomCenter)
            ) {


                Button(
                    onClick = { event(RescueResultUiEvent.CloseRescueResults) },
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
        }

    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueResultsScreenContentDark() {

    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            RescueResultsScreenContent(event = {})
        }
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewRescueResultsScreenContentLight() {

    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            RescueResultsScreenContent(event = {})
        }
    }
}

