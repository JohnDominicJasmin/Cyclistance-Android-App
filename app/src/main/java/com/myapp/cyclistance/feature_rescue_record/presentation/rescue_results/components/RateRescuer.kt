package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun RateRescuer(modifier: Modifier = Modifier, onValueChange: (Float) -> Unit, rating: Float) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "How would you rate your rescuer?",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body2)

        RatingBar(
            value = rating,
            style = RatingBarStyle.Stroke(
                activeColor = MaterialTheme.colors.primary,
            ),
            onValueChange = onValueChange,
            onRatingChanged = {},
            size = 38.dp

            )
    }
}

@Preview
@Composable
fun PreviewRateRescuer() {
    CyclistanceTheme(darkTheme = true) {
        var rating: Float by remember { mutableStateOf(0.0f) }
        RateRescuer(onValueChange = { rating = it }, rating = rating)
    }
}