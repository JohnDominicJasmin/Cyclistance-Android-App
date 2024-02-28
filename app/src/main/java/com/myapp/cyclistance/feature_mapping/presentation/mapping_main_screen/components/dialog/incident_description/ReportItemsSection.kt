package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.theme.Blue600

@Composable
fun ReportItemsSection(modifier: Modifier = Modifier, marker: HazardousLaneMarkerDetails, viewProofIncident: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)) {

        ReportItemDescription(
            modifier = Modifier
                .fillMaxWidth(),
            iconImage = Icons.Default.AccessTimeFilled,
            iconDescription = "Date and Time",
            description = marker.datePosted.toReadableDateTime(pattern = "MMM dd, yyyy hh:mm a")
        )

        ReportItemDescription(
            modifier = Modifier

                .fillMaxWidth(),
            iconImage = Icons.Default.LocationOn,
            iconDescription = "Address",
            description = marker.address
        )

        marker.description.takeIf { it.isNotEmpty() }?.let { description ->
            ReportItemDescription(
                modifier = Modifier
                    .fillMaxWidth(),
                iconImage = Icons.Default.Info,
                iconDescription = "Description",
                description = description
            )
        }

        ReportItemDescription(
            modifier = Modifier.fillMaxWidth(),
            iconImage = Icons.Default.Image,
            isTextClickable = true,
            textColor = Blue600,
            iconDescription = "Incident Image",
            description = "View proof of incident ",
            onClickText = viewProofIncident)


    }
}
