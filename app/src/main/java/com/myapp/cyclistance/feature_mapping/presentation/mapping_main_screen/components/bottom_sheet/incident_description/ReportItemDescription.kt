package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black500

@Composable
fun ReportItemDescription(
    modifier: Modifier = Modifier,
    iconImage: ImageVector,
    iconDescription: String,
    description: String = "") {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start) {


        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = iconImage,
                contentDescription = iconDescription,
                tint = Black500,
                modifier = Modifier.padding(end = 5.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = iconDescription,
                    color = Black500,
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(top = 5.dp))

                Text(
                    text = description,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.padding(vertical = 4.dp))

            }
        }

    }
}
