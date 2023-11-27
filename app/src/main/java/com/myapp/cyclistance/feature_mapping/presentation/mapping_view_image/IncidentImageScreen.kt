package com.myapp.cyclistance.feature_mapping.presentation.mapping_view_image

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import com.myapp.cyclistance.core.utils.save_images.ImageUtils
import com.myapp.cyclistance.feature_mapping.presentation.mapping_view_image.components.IncidentImageContent

@Composable
fun IncidentImageScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    photoUrl: String) {

    IncidentImageContent(
        modifier = Modifier.padding(paddingValues),
        photoBitmap = ImageUtils.decodeImage(photoUrl).asImageBitmap())
}