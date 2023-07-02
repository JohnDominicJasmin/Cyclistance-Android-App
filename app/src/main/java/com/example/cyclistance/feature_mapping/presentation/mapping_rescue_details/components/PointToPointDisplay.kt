package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun PointToPointDisplay(
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Icon(
            modifier = Modifier
                .weight(0.3f, fill = false)
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_starting_destination),
            contentDescription = "Starting Location Icon",
            tint = Color.Unspecified)

        Box(
            modifier = Modifier
                .width(width = 1.dp)
                .fillMaxHeight()
                .background(color = Black500)
                .weight(0.4f, fill = true)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_destination_location),
            contentDescription = "Destination Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .weight(0.3f, fill = false)
                .size(24.dp))

    }
}

@Preview
@Composable
fun PreviewPointToPointDIsplay() {
    CyclistanceTheme(darkTheme = true) {
        PointToPointDisplay(
            modifier = Modifier.requiredHeight(100.dp),
        )
    }
}