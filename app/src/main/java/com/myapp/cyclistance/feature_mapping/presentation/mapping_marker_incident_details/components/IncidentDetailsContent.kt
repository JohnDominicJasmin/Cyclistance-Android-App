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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun IncidentDetailsContent(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            DetailsImage(photoBitmap = ImageBitmap.imageResource(id = R.drawable.receiving_order))

            Text(
                text = "Incident Details",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview
@Composable
fun PreviewIncidentDetailsContent() {
    CyclistanceTheme(darkTheme = true) {
        IncidentDetailsContent()
    }
}