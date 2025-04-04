package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun ProfilePictureArea(photoUrl: Any, modifier: Modifier, onClick: () -> Unit) {

    Surface(
        modifier = modifier,
        color = (Color.Transparent),
        shape = CircleShape,
    ) {



            Box(modifier = Modifier.padding(all = 10.dp)) {


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
                                .clickable { onClick() }
                                .align(Alignment.Center),
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

@Preview
@Composable
fun ProfilePictureAreaPreview() {

    CyclistanceTheme(true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {

            ProfilePictureArea(
                photoUrl = MappingConstants.IMAGE_PLACEHOLDER_URL,
                modifier = Modifier.size(125.dp),
                onClick = {})
        }
    }
}