package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black500


@Composable
internal fun RescueDescription(
    modifier: Modifier = Modifier,
    iconDescription: Int,
    textDescription: String,
    bikeType: String) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(
            width = 1.dp, color = Black500
        )) {

        Row(
            modifier = Modifier

                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.5f)
            ) {

                Icon(
                    painter = painterResource(id = iconDescription),
                    contentDescription = "Description Icon",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(0.1f, fill = false)
                )
                Text(
                    text = textDescription,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle2)
            }

            Divider(
                color = MaterialTheme.colors.onSurface, modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.5f)
            ) {

                Icon(
                    imageVector = Icons.Default.PedalBike,
                    contentDescription = "Bike Icon",
                    tint = Black500,
                    modifier = Modifier.weight(0.1f, fill = false)
                )

                Text(
                    text = bikeType,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle2)
            }

        }

    }

}