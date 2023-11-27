package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker

@Composable
fun ReportItemsSection(modifier: Modifier = Modifier, marker: HazardousLaneMarker) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)) {

        ReportItemDescription(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            iconImage = Icons.Default.AccessTimeFilled,
            iconDescription = "Date and Time",
            description = marker.datePosted.toReadableDateTime(pattern = "MMM dd, yyyy hh:mm a")
        )

        ReportItemDescription(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            iconImage = Icons.Default.LocationOn,
            iconDescription = "Address",
            description = marker.address
        )


        marker.description.takeIf { it.isNotEmpty() }?.let { description ->
            ReportItemDescription(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                iconImage = Icons.Default.Info,
                iconDescription = "Description",
                description = description
            )
        }

    }
}
