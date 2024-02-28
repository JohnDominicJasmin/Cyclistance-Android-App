package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black440

@Composable
fun SpeedometerSection(
    modifier: Modifier = Modifier,
    currentSpeed: String,
    distance: String,
    maxSpeed: String) {

    Column(
        modifier = modifier.padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()) {


            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Black440,
                thickness = 1.dp,
            )

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {

                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Travelled",
                    content = distance)

                Divider(
                    color = Black440, modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp))

                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Current Speed",
                    content = currentSpeed)

                Divider(
                    color = Black440, modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp))

                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Max Speed",
                    content = maxSpeed)
            }

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Black440,
                thickness = 1.dp,
            )
        }
    }
}
