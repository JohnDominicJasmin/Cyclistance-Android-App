package com.myapp.cyclistance.feature_mapping.presentation.mapping_marker_incident_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun DetailsImage(modifier: Modifier = Modifier, photoBitmap: ImageBitmap) {


    Image(
        bitmap = photoBitmap,
        contentDescription = "User Profile Image",
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop,

        )

}

@Preview
@Composable
fun PreviewDetailsImage() {
    CyclistanceTheme(darkTheme = true) {
        DetailsImage(photoBitmap = ImageBitmap.imageResource(id = R.drawable.ic_empty_profile_placeholder_large))
    }
}


