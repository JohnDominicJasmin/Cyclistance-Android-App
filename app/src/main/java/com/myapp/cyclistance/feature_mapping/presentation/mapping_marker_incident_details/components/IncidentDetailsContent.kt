package com.myapp.cyclistance.feature_mapping.presentation.mapping_marker_incident_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.core.utils.save_images.ImageUtils
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun IncidentDetailsContent(modifier: Modifier = Modifier, marker: HazardousLaneMarkerDetails) {

    Surface(modifier = modifier, color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            DetailsImage(
                modifier = Modifier.alpha(0.6f),
                photoBitmap = ImageUtils.decodeImage(marker.incidentImageUri).asImageBitmap())

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Normal,
                text = buildAnnotatedString {
                    withStyle(style = ParagraphStyle(
                        textAlign = TextAlign.Start ,
                        lineHeight = TextUnit(23f, type = TextUnitType.Sp),


                    )){
                        append(marker.datePosted.toReadableDateTime(pattern = "dd MMM yyyy hh:mm a\n\n"))
                        append("${marker.address}\n")
                        append("${marker.latitude}° N ${marker.longitude}° E\n")
                        append("${marker.description}\n")

                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewIncidentDetailsContent() {
    CyclistanceTheme(darkTheme = true) {
        IncidentDetailsContent(marker = HazardousLaneMarkerDetails(
            id = "1",
            idCreator = "1",
            label = "Construction",
            description = "Construction",
            latitude = 14.0835,
            longitude = 121.1476,
            datePosted = Date(),
            address = "Tanauan Batangas",
            incidentImageUri = ""

        ))
    }
}