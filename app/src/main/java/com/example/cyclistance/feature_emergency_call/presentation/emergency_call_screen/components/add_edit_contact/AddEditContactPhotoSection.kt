package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.core.utils.save_images.ImageUtils
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent


@Composable
fun AddEditPhotoSection(
    isOnEditMode: Boolean,
    selectedImage: String,
    event: (EmergencyCallUiEvent) -> Unit) {


    val shouldShowAddEditImage = remember(isOnEditMode, selectedImage) {
        isOnEditMode.or(selectedImage.isNotEmpty())
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (shouldShowAddEditImage) {

            AddEditContactImage(
                photoUrl = selectedImage,

                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
                    .clickable { event(EmergencyCallUiEvent.ToggleBottomSheet) })

        } else {

            Icon(
                painter = painterResource(id = R.drawable.ic_add_to_contact),
                contentDescription = "Add new contact",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { event(EmergencyCallUiEvent.ToggleBottomSheet) }
            )

        }


        ClickableText(
            text = buildAnnotatedString {
                append(if (isOnEditMode) "Change Photo" else "Add Photo")
            },
            onClick = {
                event(EmergencyCallUiEvent.ToggleBottomSheet)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onBackground))


    }


}

@Composable
fun AddEditContactImage(
    modifier: Modifier = Modifier,
    photoUrl: String?,
) {

    Surface(
        modifier = modifier,
        color = (Color.Transparent),
        shape = CircleShape) {


        val isDefaultContact = remember(photoUrl) {
            (photoUrl == NATIONAL_EMERGENCY_PHOTO || photoUrl == PHILIPPINE_RED_CROSS_PHOTO) && photoUrl.isNotEmpty()
        }

        val imageModifier = remember {
            Modifier
                .clip(CircleShape)
                .fillMaxSize()

        }


        if (isDefaultContact) {

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
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

        } else {
            Image(
                bitmap = ImageUtils.decodeImage(photoUrl!!).asImageBitmap(),
                contentDescription = "User Profile Image",
                alignment = Alignment.Center,
                modifier = imageModifier,
                contentScale = ContentScale.Crop,

                )

        }
    }
}