package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.myapp.cyclistance.R
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.MappingBottomSheet
import com.myapp.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetOnGoingRescue(
    modifier: Modifier = Modifier,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit,
    onClickArrivedLocation: () -> Unit,
    role: String,
    onGoingRescueModel: OnGoingRescueModel,
    shouldShowArrivedLocation: Boolean
) {

    val isRescuer = remember(role) {
        role == Role.Rescuer.name
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        backgroundColor = MaterialTheme.colors.surface) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()) {

            val (time, roundedButtonSection, distance, etaIcon, speedometer, grip, arrivedButton) = createRefs()

            val etaAvailable by remember(onGoingRescueModel.estimatedTime) {
                derivedStateOf {
                    onGoingRescueModel.estimatedTime.isNotEmpty()
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.1f)
                    .padding(vertical = 4.dp)
                    .constrainAs(grip) {
                        top.linkTo(parent.top, margin = 4.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    },
                thickness = 1.dp, color = MaterialTheme.colors.primary
            )

            if (!etaAvailable) {

                Text(
                    text = "Calculating estimated time of arrival...",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(grip.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

            }

            if (etaAvailable) {

                if (isRescuer) {
                    SpeedometerSection(
                        modifier = Modifier.constrainAs(speedometer) {
                            top.linkTo(grip.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        currentSpeed = onGoingRescueModel.currentSpeed,
                        distance = onGoingRescueModel.ridingDistance,
                        maxSpeed = onGoingRescueModel.maxSpeed,
                    )
                }

                Text(
                    text = onGoingRescueModel.estimatedTime,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .constrainAs(time) {
                            top.linkTo(
                                if (isRescuer) speedometer.bottom else grip.bottom,
                                margin = 8.dp)
                            end.linkTo(etaIcon.start)
                        }
                )


                Image(
                    painter = painterResource(id = R.drawable.ic_eta),
                    contentDescription = "ETA",
                    modifier = Modifier
                        .size(20.dp)
                        .constrainAs(etaIcon) {
                            top.linkTo(
                                if (isRescuer) speedometer.bottom else grip.bottom,
                                margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            this.centerHorizontallyTo(parent)
                        }
                )


                Text(
                    text = onGoingRescueModel.estimatedDistance ?: ".....",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .constrainAs(distance) {
                            top.linkTo(
                                if (isRescuer) speedometer.bottom else grip.bottom,
                                margin = 8.dp,
                            )
                            start.linkTo(etaIcon.end)
                        }
                )
            }


            RoundButtonSection(
                modifier = Modifier.padding(bottom = 8.dp).constrainAs(roundedButtonSection) {
                    val anchor = if (etaAvailable) etaIcon else time
                    top.linkTo(anchor.bottom, margin = 12.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
                onClickCallButton = onClickCallButton,
                onClickChatButton = onClickChatButton,
                onClickCancelButton = onClickCancelButton)

            AnimatedVisibility(
                modifier = Modifier.constrainAs(arrivedButton) {
                    top.linkTo(roundedButtonSection.bottom, margin = 4.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                },
                visible = isRescuer && shouldShowArrivedLocation,
                enter = fadeIn(),
                exit = fadeOut()) {

                Button(
                    onClick = onClickArrivedLocation,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary),
                    modifier = Modifier, shape = RoundedCornerShape(8.dp)) {

                    Text(
                        text = "Arrived at the Location",
                        modifier = Modifier.padding(horizontal = 12.dp))
                }

            }



        }
    }
}








@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "BottomSheetOnGoingRescue", device = "id:Galaxy Nexus")
@Composable
private fun PreviewBottomSheetOnGoingRescueDark() {

    CyclistanceTheme(true) {

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Expanded,
                confirmStateChange = { false })
        )

        Box {

            MappingBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .align(Alignment.BottomCenter),
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                sheetGesturesEnabled = true,
                sheetPeekHeight = 55.dp,
                sheetContent = {
                    BottomSheetOnGoingRescue(
                        onClickCancelButton = {},
                        onClickCallButton = {},
                        onClickChatButton = {},
                        onClickArrivedLocation = {},
                        onGoingRescueModel = OnGoingRescueModel(
                            currentSpeed = "13.3",
                            ridingDistance = "10.0 km",
                            maxSpeed = "36 km/h",
                            estimatedDistance = "9.0 km",
                            estimatedTime = "1h 20m",
                        ),
                        role = Role.Rescuer.name, shouldShowArrivedLocation = false)
                }, content = {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)) {

                    }
                })

        }


    }
}


@Preview(name = "BottomSheetOnGoingRescue", device = "id:Nexus 5")
@Composable
private fun PreviewBottomSheetOnGoingRescueLight() {

    CyclistanceTheme(false) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {


            BottomSheetOnGoingRescue(
                onClickCancelButton = {},
                onClickCallButton = {},
                onClickChatButton = {},
                onClickArrivedLocation = {},
                onGoingRescueModel = OnGoingRescueModel(
                    currentSpeed = "13.3",
                    ridingDistance = "10.0 km",
                    maxSpeed = "36 km/h",
                    estimatedDistance = "9.0 km",
                    estimatedTime = "1h 20m",
                ),
                role = Role.Rescuee.name, shouldShowArrivedLocation = true)
        }
    }
}


