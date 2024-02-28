package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun RescueStats(
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

