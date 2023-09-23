package com.example.cyclistance.feature_rescue_record.presentation.history_details.components

import androidx.compose.foundation.layout.Arrangement
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
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun HistoryDetailsPlaceholder(
    modifier: Modifier = Modifier,
) {
    
    val isDarkTheme = IsDarkTheme.current
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "We're sorry, but we couldn't retrieve the details for this ride at this time. Please try again later or contact our support team for assistance.",
            style = MaterialTheme.typography.body1,
            color = Black500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp).fillMaxWidth(0.9f))


        Icon(
            painter = painterResource(id = if(isDarkTheme)R.drawable.ic_ride_details_placeholder_dark else R.drawable.ic_ride_details_placeholder_light),
            contentDescription = "Ride Details not found",
            tint = Color.Unspecified
        )


    }
}

@Preview(name = "Dark Theme")
@Composable
fun PreviewHistoryDetailsPlaceholder1() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            HistoryDetailsPlaceholder()
        }
    }
}

@Preview(name = "Light Theme")
@Composable
fun PreviewHistoryDetailsPlaceholder2() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            HistoryDetailsPlaceholder()
        }
    }
}