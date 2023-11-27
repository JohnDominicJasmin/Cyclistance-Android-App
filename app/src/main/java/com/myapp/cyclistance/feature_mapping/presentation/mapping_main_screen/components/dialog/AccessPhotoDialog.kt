package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessPhotoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    openGallery: () -> Unit,
    takePhoto: () -> Unit) {
    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    if (isDialogOpen) {

        Dialog(
            onDismissRequest = {
                onDismissRequest()
                onDialogVisibilityToggle(false)
            }, properties = DialogProperties(
                usePlatformDefaultWidth = true,
            )) {


            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(all = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterVertically)) {

                AccessPhotoItem(
                    modifier = Modifier.fillMaxWidth(),
                    imageId = R.drawable.ic_gallery,
                    descriptionText = "Open Gallery",
                    onClick = openGallery)
                AccessPhotoItem(
                    modifier = Modifier.fillMaxWidth(),
                    imageId = R.drawable.ic_camera,
                    descriptionText = "Take Photo",
                    onClick = takePhoto)
            }
        }
    }
}


@Composable
private fun AccessPhotoItem(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    descriptionText: String,
    onClick: () -> Unit) {


    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.surface),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.5.dp,

            )) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {


            Icon(
                painter = painterResource(id = imageId),
                contentDescription = "Access photo icon",
                modifier = Modifier
                    .padding(all = 4.dp)
                    .padding(end = 8.dp)
                    .size(32.dp),
                tint = MaterialTheme.colors.primary
            )

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = descriptionText,
                style = MaterialTheme.typography.button.copy(color = MaterialTheme.colors.onSurface),
            )
        }
    }
}

@Preview
@Composable
fun PreviewAccessPhotoItem() {
    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface),
            contentAlignment = Alignment.Center) {}
        AccessPhotoItem(
            imageId = R.drawable.ic_gallery,
            descriptionText = "Open Gallery",
            onClick = {}
        )
    }
}


@Preview
@Composable
fun PreviewAccessPhotoDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessPhotoDialog(onDismissRequest = {}, openGallery = {}, takePhoto = {})
    }
}