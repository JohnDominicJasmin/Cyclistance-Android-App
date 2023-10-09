package com.example.cyclistance.feature_messaging.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MessageUserImage(
    modifier: Modifier = Modifier,
    photoUrl: String?) {

    Box(modifier = Modifier.wrapContentSize()) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "User Profile Image",
            modifier = modifier
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
            placeholder = rememberAsyncImagePainter(model = R.drawable.ic_empty_profile_placeholder_large),
            error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))



    }
}

@Preview
@Composable
fun PreviewUserImage() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
            MessageUserImage(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(CircleShape)
                    .size(48.dp),
                photoUrl = MappingConstants.IMAGE_PLACEHOLDER_URL)
        }
    }
}