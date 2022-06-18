package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cyclistance.R


@Composable
fun ProfilePictureArea(photoUrl: Any, modifier: Modifier, onClick: () -> Unit) {


    Surface(modifier = modifier
        .clip(CircleShape)) {

        Column(
            verticalArrangement = Arrangement.spacedBy(
                6.5.dp,
                alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier.padding(all = 10.dp)) {


                when(photoUrl){

                    is String -> {
                        AsyncImage(
                            model = photoUrl,
                            alignment = Alignment.Center,
                            contentDescription = "User Profile Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onClick() }
                                .align(Alignment.Center),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder),
                            error = painterResource(id = R.drawable.ic_empty_profile_placeholder),
                            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder))
                    }

                    is ImageBitmap -> {
                      Image(
                          bitmap = photoUrl,
                          contentDescription = "User Profile Image",
                          alignment = Alignment.Center,
                          modifier = Modifier
                              .clip(CircleShape)
                              .clickable { onClick() }
                              .align(Alignment.Center),
                          contentScale = ContentScale.Crop,

                      )
                    }
                }


                Image(
                    painter = painterResource(id = R.drawable.ic_picture_frame),
                    contentDescription = "Profile Picture Frame",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }

        }
    }
}
