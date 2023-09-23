package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RideHistoryPlaceHolder(modifier: Modifier = Modifier) {

    val isDarkTheme = IsDarkTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            color = MaterialTheme.colors.onBackground,
            text = "You have not taken any rides yet. Let’s start by responding or requesting a help to nearby cyclist.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(0.9f),
            style = MaterialTheme.typography.body1
        )

        Icon(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_ride_history_placeholder_dark else R.drawable.ic_ride_history_placeholder_light),
            contentDescription = "Ride History Place Holder", tint = Color.Unspecified)


    }
}

@Preview
@Composable
fun PreviewRideHistoryPlaceHolderDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center) {
                RideHistoryPlaceHolder(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
fun PreviewRideHistoryPlaceHolderLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center) {
                RideHistoryPlaceHolder(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


