package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.DICE_BEAR_URL
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.navigation.IsDarkTheme


@Composable
fun AddEditPhotoSection(
    isOnEditMode: Boolean,
    emergencyContact: EmergencyContactModel?,
    event: (EmergencyCallUiEvent) -> Unit) {


    val isDarkTheme = IsDarkTheme.current

    val shouldShowAddEditImage = remember(isOnEditMode, emergencyContact?.photo) {
        isOnEditMode.or(emergencyContact?.photo?.isNotEmpty() == true)
    }

    val imageModel = remember(emergencyContact?.name, emergencyContact?.photo) {
        if (emergencyContact?.photo != EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO &&
            emergencyContact?.photo != EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO) {

            "$DICE_BEAR_URL${emergencyContact?.name}"
        } else {
            emergencyContact.photo
        }
    }


    if (shouldShowAddEditImage) {

        AddEditContactImage(
            photoUrl = imageModel,
            event = event,
            modifier = Modifier.size(125.dp))

    } else {

        Icon(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_add_new_contact_dark else R.drawable.ic_add_new_contact_light),
            contentDescription = "Add new contact",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { event(EmergencyCallUiEvent.ToggleBottomSheet) }
        )

    }


}

@Composable
private fun AddEditContactImage(
    modifier: Modifier = Modifier,
    photoUrl: String?,
    event: (EmergencyCallUiEvent) -> Unit) {
    Surface(
        modifier = modifier,
        color = (Color.Transparent),
        shape = CircleShape) {

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
                .clickable { event(EmergencyCallUiEvent.ToggleBottomSheet) },
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

    }
}