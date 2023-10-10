package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_request.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.formatter.FormatterUtils.formatToTimeAgo
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun RequestItem(
    modifier: Modifier,
    cardState: RescueRequestItemModel,
    onClickCancelButton: () -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
    viewProfile: () -> Unit = {}) {

    Card(
        modifier = modifier,
        elevation = 7.dp,
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 5.dp), horizontalAlignment = Alignment.End) {


            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                text = formatToTimeAgo(cardState.timestamp),
                color = Black500,
                style = MaterialTheme.typography.caption
            )

            Row(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(cardState.profileImageUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "User Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable(onClick = viewProfile))






                Text(textAlign = TextAlign.Start,
                    text = buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("${cardState.name}\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.distance ?: "----")
                        }
                    })
                Spacer(modifier = Modifier.weight(weight = 0.3f))

                Text(textAlign = TextAlign.End,
                    modifier = Modifier,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("ETA\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.estimatedTimeTravel ?: "----")
                        }

                    })

            }



            Divider(
                color = Black450,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.92f)
                    .padding(top = 12.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                thickness = (1.5).dp)


            Row(
                modifier = Modifier.padding(top = 12.dp).padding(horizontal = 15.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Address Icon",
                    tint = Color.Unspecified,
                )
                Text(text = cardState.address ?: "----", color = MaterialTheme.colors.onSurface)
            }

            ButtonNavigation(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 7.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                negativeButtonText = "Decline",
                positiveButtonText = "Accept",
                onClickNegativeButton = onClickCancelButton,
                onClickPositiveButton = onClickConfirmButton)

        }
    }

}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun RequestItemPreviewDark() {

    CyclistanceTheme(true) {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                RequestItem(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f)
                        .wrapContentHeight(),
                    cardState = RescueRequestItemModel(
                        profileImageUrl = "https://i.imgur.com/7bMqysJ.jpg",
                        name = "John Doe",
                        distance = "1.2 km",
                        estimatedTimeTravel = "5 min",
                        address = "1234, Street Name, City Name, Country Name", timestamp = 1695288153722),
                )
            }
        }
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun RequestItemPreviewLight() {
    CyclistanceTheme(false) {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                RequestItem(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f)
                        .wrapContentHeight(),
                    cardState = RescueRequestItemModel(
                        profileImageUrl = "https://i.imgur.com/7bMqysJ.jpg",
                        name = "John Doe",
                        distance = "1.2 km",
                        estimatedTimeTravel = "5 min",
                        address = "1234, Street Name, City Name, Country Name"),
                )
            }
        }
    }
}
