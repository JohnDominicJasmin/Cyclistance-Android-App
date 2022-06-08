package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.cyclistance.theme.Shapes
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.CyclistanceTheme


@ExperimentalAnimationApi
@Composable
fun MappingDefaultBanner(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)) {

            Text(
                modifier = Modifier.padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp),
                text = "John Doe cancelled the rescue request. The reason is “Problem already fixed.\"",
                color = MaterialTheme.colors.onSurface, style = MaterialTheme.typography.body2)

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)) {

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { }) {

                    Text(
                        text = "DISMISS",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle2)
                }

            }
            Divider()
        }
    }
}


data class MappingBannerData(
    val userProfileImage: Int,
    val name: String,
    val issue: String,
    val distanceRemaining: String,
    val timeRemaining: String,
    val address: String,
    val message: String
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingExpandableBanner(bannerModel: MappingBannerData) {


    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )
    val bannerModelState = remember { bannerModel }

    with(bannerModelState) {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 180,
                        easing = LinearOutSlowInEasing
                    )
                ),
            shape = Shapes.medium,
            onClick = { expandedState = !expandedState }, backgroundColor = MaterialTheme.colors.surface) {

            Column(modifier = Modifier.fillMaxWidth()) {

                IconButton(
                    onClick = { expandedState = !expandedState },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState)
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        tint = MaterialTheme.colors.onSurface,
                        contentDescription = "Drop down icon",
                        modifier = Modifier.size(30.dp))

                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, bottom = 12.dp, end = 12.dp)) {

                    val (roundedImage, nameText, issueText, distanceAndTime, expandedSection, dismissButton) = createRefs()



                    Image(
                        painter = painterResource(userProfileImage),
                        contentDescription = "User Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .constrainAs(roundedImage) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                    )

                    Text(
                        text = name,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.constrainAs(nameText) {
                            start.linkTo(roundedImage.end, margin = 15.dp)
                            top.linkTo(parent.top, margin = 13.dp)

                        }
                    )


                    Text(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Black440)) {
                                append("Issue:")
                            }

                            withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
                                append(" ")
                                append(issue)
                            }

                        }, modifier = Modifier.constrainAs(issueText) {
                            top.linkTo(nameText.bottom)
                            start.linkTo(roundedImage.end, margin = 15.dp)
                        })


                    Text(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
                                append(distanceRemaining)
                            }

                            withStyle(style = SpanStyle(color = Black440)) {
                                append("\n" + timeRemaining)
                            }

                        }, modifier = Modifier.constrainAs(distanceAndTime) {
                            top.linkTo(parent.top, margin = 13.dp)
                            end.linkTo(parent.end, margin = 5.dp)
                        })


                    if (expandedState) {

                        Column(modifier = Modifier.constrainAs(expandedSection) {
                            top.linkTo(issueText.bottom, margin = 10.dp)
                            width = Dimension.matchParent
                            bottom.linkTo(parent.bottom, margin = 15.dp)
                        }) {

                            Divider(
                                color = Black450,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    alignment = Alignment.Start)) {

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_location),
                                    contentDescription = "Location Icon",
                                    modifier = Modifier.wrapContentSize(), tint = Color.Unspecified)

                                Text(
                                    text = address,
                                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }

                            Divider(
                                color = Black450,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))


                            Text(
                                text = "Message:",
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.Normal,
                                color = Black440
                            )
                            Text(
                                text = message,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface,
                            )
                        }
                    }


                }

                TextButton(
                    modifier = Modifier.padding(end = 8.dp)
                        .align(Alignment.End),
                    onClick = { }) {
                    Text(
                        text = "DISMISS",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle2)
                }
            }
        }
    }
}

@Preview
@Composable
fun MappingExpandableBannerPreview() {
    CyclistanceTheme(true) {
        MappingExpandableBanner(
            bannerModel = MappingBannerData(
                userProfileImage = R.drawable.person_image,
                name = "John Doe",
                issue = "Faulty Brakes",
                distanceRemaining = "7.0km",
                timeRemaining = "2mins",
                address = "Taal Batangas",
                message = "Lorem ipsum dolor sit amet, consectetur adipis consectetur adipis adipis consectetur adipis adipis adipis adip"))
    }

}

/*
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun DefaultBannerPreview() {
    CyclistanceTheme(true) {
        MappingDefaultBanner(isVisible = true)
    }
}
*/
