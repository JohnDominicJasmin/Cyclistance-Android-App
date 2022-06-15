package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cyclistance.R


@Composable
fun ProfilePictureArea(photoUrl: String, modifier: Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.5.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Box {

            AsyncImage(
                model =  photoUrl,
                alignment = Alignment.Center,
                contentDescription = "User Profile Image",
                modifier = Modifier.clip(CircleShape).align(Alignment.Center),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder))

            Image(
                painter = painterResource(id = R.drawable.ic_picture_frame),
                contentDescription = "Profile Picture Frame",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop)
        }

    }
}
