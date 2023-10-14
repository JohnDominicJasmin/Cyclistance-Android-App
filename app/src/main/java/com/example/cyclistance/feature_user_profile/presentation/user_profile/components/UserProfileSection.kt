package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun UserProfileSection(
    modifier: Modifier = Modifier,
    state: UserProfileState,
    onClickEditProfile: () -> Unit,
    onClickRideHistory: () -> Unit,
) {


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
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(0.9f),
            verticalArrangement = Arrangement.spacedBy(
                2.dp,
                alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start) {

            Text(
                text = state.userProfileModel.getName(),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = 3.5.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2

            )

            RatingBar(
                modifier = Modifier
                    .padding(start = 3.5.dp)
                    .padding(vertical = 4.dp),
                value = state.userProfileModel.getAverageRating().toFloat(),
                style = RatingBarStyle.Stroke(
                    activeColor = MaterialTheme.colors.primary,
                ),
                onValueChange = {},
                onRatingChanged = {},
                size = 14.dp,
                spaceBetween = 2.dp

            )

            val address = state.userProfileModel.getAddress()
            val bikeGroup = state.userProfileModel.getBikeGroup()

            if (address?.isNotEmpty() == true) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        alignment = Alignment.CenterHorizontally)) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "User Address",
                        tint = MaterialTheme.colors.onBackground)
                    Text(
                        text = address,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }

            if (bikeGroup?.isNotEmpty() == true) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        alignment = Alignment.CenterHorizontally)) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsBike,
                        contentDescription = "User Bike Group",
                        tint = MaterialTheme.colors.onBackground)
                    Text(
                        text = bikeGroup,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }


        }



        Column(
            modifier = Modifier
                .wrapContentSize(align = Alignment.TopEnd)
                .weight(0.2f)) {

            val isYourProfile = state.userId == state.profileSelectedId
            if (isYourProfile) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onClickEditProfile() }
                        .padding(all = 8.dp)
                        .wrapContentSize(align = Alignment.TopCenter))
            }

            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "Ride History",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickRideHistory() }
                    .padding(all = 8.dp)
                    .wrapContentSize(align = Alignment.TopCenter))
        }


    }

}