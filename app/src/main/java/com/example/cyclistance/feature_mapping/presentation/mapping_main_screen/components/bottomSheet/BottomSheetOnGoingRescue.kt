package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red900

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetOnGoingRescue(
    modifier: Modifier = Modifier,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onGoingRescueModel: OnGoingRescueModel,
) {


    Card(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        backgroundColor = MaterialTheme.colors.surface) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()) {

            val (time, roundedButtonSection, lineGrip, distance, etaIcon, speedometer) = createRefs()

            val etaAvailable by remember(onGoingRescueModel.estimatedTime) {
                derivedStateOf {
                    onGoingRescueModel.estimatedTime.isNotEmpty()
                }
            }

            Divider(modifier = Modifier
                .fillMaxWidth(0.1f)
                .constrainAs(lineGrip) {
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }, color = MaterialTheme.colors.primary, thickness = 1.5.dp)


            if (!etaAvailable) {

                Text(
                    text = "Calculating estimated time of arrival...",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(lineGrip.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

            }

            if (etaAvailable) {
                SpeedometerSection(
                    modifier = Modifier.constrainAs(speedometer) {
                        top.linkTo(lineGrip.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    currentSpeed = onGoingRescueModel.currentSpeed,
                    distance = onGoingRescueModel.ridingDistance,
                    maxSpeed = onGoingRescueModel.maxSpeed,
                    time = onGoingRescueModel.ridingTime
                )

                Text(
                    text = onGoingRescueModel.estimatedTime,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .constrainAs(time) {
                            top.linkTo(speedometer.bottom, margin = 5.dp)
                            end.linkTo(etaIcon.start)
                        }
                )


                Image(
                    painter = painterResource(id = R.drawable.ic_eta),
                    contentDescription = "ETA",
                    modifier = Modifier
                        .size(20.dp)
                        .constrainAs(etaIcon) {
                            top.linkTo(speedometer.bottom, margin = 5.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        }
                )


                Text(
                    text = onGoingRescueModel.estimatedDistance,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .constrainAs(distance) {
                            top.linkTo(speedometer.bottom, margin = 5.dp)
                            start.linkTo(etaIcon.end)
                        }
                )
            }


            RoundButtonSection(
                modifier = Modifier.constrainAs(roundedButtonSection) {
                    val anchor = if (etaAvailable) etaIcon else time
                    top.linkTo(anchor.bottom, margin = 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                },
                onClickCallButton = onClickCallButton,
                onClickChatButton = onClickChatButton,
                onClickCancelButton = onClickCancelButton)


        }
    }
}


@Composable
fun SpeedometerSection(
    modifier: Modifier = Modifier,
    currentSpeed: String,
    distance: String,
    maxSpeed: String,
    time: String) {

    Column(
        modifier = modifier.padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Text(
            text = "Current Speed",
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.caption.fontSize),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(vertical = 1.dp))

        Text(color = MaterialTheme.colors.onSurface, text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Medium)) {
                append(currentSpeed)
            }
            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.overline.fontSize)) {
                append(" km/h")
            }
        }, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(vertical = 1.dp))


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()) {

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Black440,
                thickness = 1.dp,
            )

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {

                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Distance",
                    content = distance)

                Divider(
                    color = Black440, modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp))


                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Max Speed",
                    content = maxSpeed)

                Divider(
                    color = Black440, modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp))

                ItemSpeed(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(0.3f),
                    title = "Time",
                    content = time)

            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Black440,
                thickness = 1.dp,
            )
        }
    }
}

@Composable
fun RowScope.ItemSpeed(modifier: Modifier, title: String, content: String) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .weight(0.3f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.caption.fontSize))

        Text(
            text = content,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.button.fontSize))
    }
}

@Composable
private fun RoundButtonSection(
    modifier: Modifier = Modifier,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit) {


    Row(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly) {

        RoundedButtonItem(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            imageId = R.drawable.ic_call,
            buttonSubtitle = "Call", onClick = onClickCallButton)


        RoundedButtonItem(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            imageId = R.drawable.ic_chat,
            buttonSubtitle = "Chat", onClick = onClickChatButton)


        RoundedButtonItem(
            backgroundColor = Red900,
            contentColor = Color.White,
            imageId = R.drawable.ic_cancel_1,
            buttonSubtitle = "Cancel", onClick = onClickCancelButton)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "BottomSheetOnGoingRescue", device = "id:Galaxy Nexus")
@Composable
private fun PreviewBottomSheetOnGoingRescueDark() {

    CyclistanceTheme(true) {
        BottomSheetOnGoingRescue(
            onClickCancelButton = {},
            onClickCallButton = {},
            onClickChatButton = {},
            bottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
            onGoingRescueModel = OnGoingRescueModel(
                currentSpeed = "13.3",
                ridingDistance = "10.0 km",
                maxSpeed = "36 km/h",
                ridingTime = "1:30:00",
                estimatedDistance = "9.0 km",
                estimatedTime = "1h 20m",

                ))
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "BottomSheetOnGoingRescue", device = "id:Galaxy Nexus")
@Composable
private fun PreviewBottomSheetOnGoingRescueLight() {

    CyclistanceTheme(false) {
        BottomSheetOnGoingRescue(
            onClickCancelButton = {},
            onClickCallButton = {},
            onClickChatButton = {},
            bottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
            onGoingRescueModel = OnGoingRescueModel(
                currentSpeed = "13.3",
                ridingDistance = "10.0 km",
                maxSpeed = "36 km/h",
                ridingTime = "1:30:00",
                estimatedDistance = "9.0 km",
                estimatedTime = "1h 20m",

                ))
    }
}


