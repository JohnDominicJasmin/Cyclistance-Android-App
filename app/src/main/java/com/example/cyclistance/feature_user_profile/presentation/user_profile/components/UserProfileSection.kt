package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun UserProfileSection(modifier: Modifier = Modifier, state: UserProfileState) {


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top) {


        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.userProfileModel.getPhoto())
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "User Picture",
            modifier = Modifier
                .size(85.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))


        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(
                2.dp,
                alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start) {

            Text(
                text = state.userProfileModel.getName() ?: "",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = 3.5.dp)

            )

            RatingBar(
                modifier = Modifier
                    .padding(start = 3.5.dp)
                    .padding(vertical = 4.dp),
                value = state.userProfileModel.getAverageRating()!!.toFloat(),
                style = RatingBarStyle.Stroke(
                    activeColor = MaterialTheme.colors.primary,
                ),
                onValueChange = {},
                onRatingChanged = {},
                size = 14.dp,
                spaceBetween = 2.dp

            )


            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "User Address",
                    tint = MaterialTheme.colors.onBackground)
                Text(
                    text = state.userProfileModel.getAddress()!!,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2)
            }


            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Outlined.DirectionsBike,
                    contentDescription = "User Bike Group",
                    tint = MaterialTheme.colors.onBackground)
                Text(
                    text = state.userProfileModel.getBikeGroup()!!,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.wrapContentSize(align = Alignment.TopEnd)) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Profile",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(all = 8.dp))

            Icon(
                imageVector = Icons.Outlined.Message,
                contentDescription = "Edit Profile",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(all = 8.dp))
        }


    }
}