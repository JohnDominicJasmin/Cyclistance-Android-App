package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditContactUiEvent


@Composable
fun AddEditPhotoSection(

    isOnEditMode: Boolean,
    photoUrl: Any?,
    event: (AddEditContactUiEvent) -> Unit) {


    val shouldShowAddEditImage = remember(isOnEditMode, photoUrl) {
        isOnEditMode.or(photoUrl != null)
    }

    if (shouldShowAddEditImage) {
        AddEditContactImage(
            photoUrl = photoUrl, event = event, modifier = Modifier
                .size(125.dp))
    } else {

        Icon(
            painter = painterResource(id = R.drawable.ic_add_to_contact),
            contentDescription = "Add new contact",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { event(AddEditContactUiEvent.ToggleBottomSheet) }
        )

    }


}

@Composable
private fun AddEditContactImage(
    modifier: Modifier = Modifier,
    photoUrl: Any?,
    event: (AddEditContactUiEvent) -> Unit) {
    Surface(
        modifier = modifier,
        color = (Color.Transparent),
        shape = CircleShape,
    ) {
        when (photoUrl) {
            is String -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    alignment = Alignment.Center,
                    contentDescription = "User Profile Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                        .clickable { event(AddEditContactUiEvent.ToggleBottomSheet) },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))
            }

            is ImageBitmap -> {
                Image(
                    bitmap = photoUrl,
                    contentDescription = "User Profile Image",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                        .size(100.dp)
                        .clickable { event(AddEditContactUiEvent.ToggleBottomSheet) },
                    contentScale = ContentScale.Crop,

                    )
            }
        }
    }
}