@file:OptIn(ExperimentalMaterialApi::class)

package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.AnimatedImage
import com.example.cyclistance.theme.Black300
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red900
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    sheetPeekHeight: Dp = 0.dp,
    sheetGesturesEnabled: Boolean = true,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = sheetContent,
        sheetPeekHeight = sheetPeekHeight,
        sheetGesturesEnabled = sheetGesturesEnabled,
        content = content,
        modifier = modifier,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,


        )


}


@Composable
fun BottomSheetSearchingAssistance(
    isDarkTheme: Boolean,
    content: @Composable (PaddingValues) -> Unit,
    onClickCancelButton: () -> Unit) {

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Expanded))
    val scope = rememberCoroutineScope()
    MappingBottomSheet(
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp), elevation = 14.dp) {


                ConstraintLayout(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)) {

                    val (searchAnimatedIcon, searchingText, cancelButton) = createRefs()

                    AnimatedImage(
                        imageId = if (isDarkTheme) R.drawable.ic_dark_magnifying_glass else R.drawable.ic_light_magnifying_glass,
                        modifier = Modifier
                            .constrainAs(searchAnimatedIcon) {
                                top.linkTo(parent.top, margin = 4.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                height = Dimension.value(50.dp)
                                width = Dimension.value(50.dp)
                            })




                    Text(
                        text = "Searching for nearby assistance...",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .constrainAs(searchingText) {
                                top.linkTo(searchAnimatedIcon.bottom, margin = 4.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                            })

                    OutlinedActionButton(
                        modifier = Modifier
                            .padding(3.dp)
                            .constrainAs(cancelButton) {
                                top.linkTo(searchingText.bottom, margin = 8.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom, margin = 6.dp)
                            },
                        onClick = {
                            onClickCancelButton()
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                        text = "Cancel"
                    )
                }
            }
        }, content = content)
}


@Composable
fun BottomSheetOnGoingRescue(
    content: @Composable (PaddingValues) -> Unit,
    estimatedTimeRemaining: String) {


    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Expanded))

    MappingBottomSheet(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp), elevation = 14.dp) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.surface)) {

                    val (timeRemaining, roundedButtonSection) = createRefs()



                    Text(
                        text = "Estimated time of arrival: $estimatedTimeRemaining",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.constrainAs(timeRemaining) {
                            top.linkTo(parent.top, margin = 14.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })


                    Row(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                            .constrainAs(roundedButtonSection) {
                                top.linkTo(timeRemaining.bottom, margin = 15.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom, margin = 17.dp)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly) {

                        RoundedButtonItem(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                            imageId = R.drawable.ic_call,
                            buttonSubtitle = "Call", onClick = {})


                        RoundedButtonItem(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                            imageId = R.drawable.ic_chat,
                            buttonSubtitle = "Chat", onClick = {})


                        RoundedButtonItem(
                            backgroundColor = Red900,
                            contentColor = Color.White,
                            imageId = R.drawable.ic_cancel,
                            buttonSubtitle = "Cancel", onClick = {})
                    }
                }
            }
        },
        content = content)

}

@Composable
fun BottomSheetRescueArrived(
    isDarkTheme: Boolean,
    content: @Composable (PaddingValues) -> Unit,
    onClickOkButton: () -> Unit) {

    BottomSheetRescue(
        isDarkTheme = isDarkTheme,
        displayedText = "Your rescuer has arrived.",
        content = content, onClickOkButton = onClickOkButton)
}

@Composable
fun BottomSheetReachedDestination(
    isDarkTheme: Boolean,
    content: @Composable (PaddingValues) -> Unit,
    onClickOkButton: () -> Unit) {

    BottomSheetRescue(
        isDarkTheme = isDarkTheme,
        displayedText = "You have reached your destination.",
        content = content, onClickOkButton = onClickOkButton)
}


@Composable
private fun BottomSheetRescue(
    isDarkTheme: Boolean,
    onClickOkButton: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    displayedText: String) {


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Expanded))

    val scope = rememberCoroutineScope()


    MappingBottomSheet(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp), elevation = 14.dp) {


                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.surface)) {

                    val (gripLine, animatedIcon, arrivedText, cancelButton) = createRefs()

                    Divider(
                        modifier = Modifier.constrainAs(gripLine) {
                            top.linkTo(parent.top, margin = 8.5.dp)
                            width = Dimension.percent(0.08f)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)

                        },
                        color = Black440,
                        thickness = 1.5.dp)

                    AnimatedImage(
                        imageId = if (isDarkTheme) R.drawable.ic_dark_ellipsis else R.drawable.ic_light_ellipsis,
                        modifier = Modifier
                            .constrainAs(animatedIcon) {
                                top.linkTo(gripLine.bottom, margin = 3.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                width = Dimension.value(55.dp)

                            })

                    Text(
                        text = displayedText,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .constrainAs(arrivedText) {
                                top.linkTo(animatedIcon.bottom, margin = 4.dp)
                                end.linkTo(parent.end)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                            })


                    OutlinedActionButton(
                        modifier = Modifier.constrainAs(cancelButton) {
                            top.linkTo(arrivedText.bottom, margin = 8.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            width = Dimension.value(80.dp)
                            bottom.linkTo(parent.bottom, margin = 6.dp)
                        },
                        onClick = {
                            onClickOkButton()
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                        text = "Ok"
                    )
                }
            }
        }, content = content)


}

@Composable
private fun OutlinedActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Black300),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )


    }
}

@Composable
private fun RoundedButtonItem(
    backgroundColor: Color,
    contentColor: Color,
    imageId: Int,
    buttonSubtitle: String,
    onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 7.dp,
            alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            modifier = Modifier
                .size(56.dp)
                .shadow(elevation = 8.dp, shape = CircleShape),
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor)) {

            Icon(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier.size(45.dp))
        }


        Text(
            text = buttonSubtitle,
            color = Black440,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center)
    }


}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomSheetRescuerArrivedPreview() {
    val isDarkTheme = true
    CyclistanceTheme(isDarkTheme) {
        BottomSheetRescueArrived(
            isDarkTheme = isDarkTheme,
            content = {}, onClickOkButton = {})
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DestinationReachedBottomSheetPreview() {
    val isDarkTheme = true
    CyclistanceTheme(isDarkTheme) {
        BottomSheetReachedDestination(
            isDarkTheme = isDarkTheme,
            content = {}, onClickOkButton = {})

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchingAssistancePreview() {
    val isDarkTheme = true
    CyclistanceTheme(isDarkTheme) {
        BottomSheetSearchingAssistance(
            isDarkTheme = isDarkTheme,
            onClickCancelButton = {},
            content = {})
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OnGoingRescueBottomSheetPreview() {
    CyclistanceTheme(true) {
        BottomSheetOnGoingRescue(estimatedTimeRemaining = "2mins", content = {})
    }
}


