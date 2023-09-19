package com.example.cyclistance.feature_rescue_outcome.presentation.rescue_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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