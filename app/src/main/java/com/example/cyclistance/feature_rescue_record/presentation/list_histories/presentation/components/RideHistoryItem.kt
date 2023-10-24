package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.formatter.IconFormatter.rescueDescriptionToIcon
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistoryItem
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.components.PointToPointDisplay
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RideHistoryItem(
    modifier: Modifier = Modifier,
    rideHistoryItem: RideHistoryItem,
    onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        onClick = onClick, elevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.Center) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(rideHistoryItem.photoUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "User Picture",
                    modifier = Modifier
                        .size(53.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        append("${rideHistoryItem.date}\n")
                        append("Duration: ${rideHistoryItem.duration}\n")
                        append("Role: ${rideHistoryItem.role}")
                    }
                }, color = MaterialTheme.colors.onSurface)

                Spacer(modifier = Modifier.weight(0.1f))

                Icon(
                    painter = painterResource(id = rideHistoryItem.rescueDescription.rescueDescriptionToIcon() ?: R.drawable.not_available_circle_svgrepo_com),
                    contentDescription = "Icon Description",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colors.onSurface
                )


            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                thickness = 1.dp,
                color = Black500)

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                PointToPointDisplay(modifier = Modifier.fillMaxHeight())

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = rideHistoryItem.startingAddress,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,)


                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = rideHistoryItem.destinationAddress,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRideHistoryItem() {

    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter) {
                RideHistoryItem(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .wrapContentHeight(),
                    rideHistoryItem = RideHistoryItem(
                        id = "001",
                        photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
                        date = "31, August, 11:30 AM",
                        duration = "28 min",
                        role = "Rescuer",
                        rescueDescription = MappingConstants.FAULTY_BRAKES_TEXT,
                        startingAddress = "Lipa City Batangas, Lipa City Batangas, Lipa City Batangas, Lipa City Batangas, Lipa City Batangas",
                        destinationAddress = "Lima Batangas, Lima Batangas, Lima Batangas, Lima Batangas, Lima Batangas",
                    ), onClick = {})
            }
        }
    }
}