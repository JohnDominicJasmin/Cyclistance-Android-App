package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping_screen.domain.model.MappingBannerData
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingExpandableBanner(modifier: Modifier = Modifier, banner: MappingBannerData) {


    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )



    Card(
        modifier = modifier
            .padding(all = 6.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 180,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = { expandedState = !expandedState },
        backgroundColor = MaterialTheme.colors.surface) {

        Column(modifier = Modifier.fillMaxWidth()) {


            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "Drop down icon",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alpha(ContentAlpha.medium)
                    .rotate(rotationState)
                    .clip(CircleShape)
                    .size(30.dp)
                    .clickable {
                        expandedState = !expandedState
                    })



            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()


            ) {

                val (roundedImage, nameText, issueText, distanceAndTime, expandedSection, dismissButton) = createRefs()



                Image(
                    painter = painterResource(banner.userProfileImage),
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
                    text = banner.name,
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
                            append(banner.issue)
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
                            append(banner.distanceRemaining)
                        }

                        withStyle(style = SpanStyle(color = Black440)) {
                            append("\n" + banner.timeRemaining)
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
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                alignment = Alignment.Start)) {

                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = "Location Icon",
                                modifier = Modifier
                                    .padding(horizontal = 1.5.dp)
                                    .wrapContentWidth()
                                    .padding(horizontal = 1.5.dp), tint = Black500)

                            Text(
                                text = banner.address,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface,
                            )
                        }

                        Divider(
                            color = Black450,
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                alignment = Alignment.Start)) {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_pedal_bike_24),
                                contentDescription = "Location Icon",
                                modifier = Modifier
                                    .padding(horizontal = 1.5.dp)
                                    .wrapContentWidth()
                                    .padding(horizontal = 1.5.dp), tint = Black500)

                            Text(
                                text = banner.bikeType,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface,
                            )
                        }


                        val hasMessage = remember(banner.message){
                            banner.message.isNotEmpty()
                        }

                        if(hasMessage){

                            Divider(
                            color = Black450,
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

                            Text(
                                text = "Message:",
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.Normal,
                                color = Black440
                            )
                            Text(
                                text = banner.message,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface,
                            )
                        }

                    }
                }


            }

            TextButton(
                modifier = Modifier
                    .padding(end = 8.dp)
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

@Preview
@Composable
fun MappingExpandableBannerPreview() {
    CyclistanceTheme(true) {
        MappingExpandableBanner(
            banner = MappingBannerData(
                userProfileImage = R.drawable.person_image,
                name = "John Doe",
                issue = "Faulty Brakes",
                distanceRemaining = "7.0km",
                timeRemaining = "2mins",
                address = "Taal Batangas",
                message = "",
                bikeType = "Road Bike",

                )
        )
    }

}