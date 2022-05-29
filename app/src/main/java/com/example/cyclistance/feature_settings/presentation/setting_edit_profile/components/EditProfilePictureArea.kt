package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.ChangeProfileTextColor


@Composable
fun ProfilePictureArea(modifier: Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.5.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Box {

            Image(
                painter = painterResource(R.drawable.person_image),
                contentDescription = "Profile_Screen",
                modifier = Modifier
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Image(
                painter = painterResource(id = R.drawable.ic_picture_frame),
                contentDescription = "Profile Picture Frame",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop)
        }

    }
}
