package com.myapp.cyclistance.feature_mapping.presentation.mapping_view_image.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun IncidentImageContent(modifier: Modifier = Modifier, photoBitmap: ImageBitmap) {

    Image(
        bitmap = photoBitmap,
        contentDescription = "User Profile Image",
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(),
        contentScale = ContentScale.Fit,

        )
}