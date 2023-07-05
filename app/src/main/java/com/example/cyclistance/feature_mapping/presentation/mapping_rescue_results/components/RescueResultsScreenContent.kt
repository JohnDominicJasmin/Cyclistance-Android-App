package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_results.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueResultsScreenContent(modifier: Modifier = Modifier) {
    var step by rememberSaveable { mutableIntStateOf(1) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            AnimatedVisibility(
                visible = step != 5,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .weight(0.25f)
                    .animateContentSize()) {
                RescueArrivedSection(
                    modifier = Modifier
                )
            }


            AnimatedVisibility(
                modifier = Modifier
                    .weight(0.25f)
                    .animateContentSize(),
                visible = step == 1,

                enter = fadeIn(),
                exit = fadeOut()) {


                RescueReportAccountSection(
                    photoUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    name = "John Doe",
                    onClickReportAccount = {
                        //todo
                    }, modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .animateContentSize())

            }

            AnimatedVisibility(
                visible = step == 2,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .weight(0.25f)
                    .animateContentSize()) {
                RescueGoodToHearSection()
            }

            ButtonNavigation(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(0.25f)
                    .animateContentSize(),
                negativeButtonText = "No",
                positiveButtonText = "Yes",
                onClickCancelButton = {/*todo*/ },
                onClickConfirmButton = { step += 1 })
        }


    }
}

@Preview
@Composable
fun PreviewRescueResultsScreenContent() {
    CyclistanceTheme(darkTheme = true) {
        RescueResultsScreenContent()
    }
}