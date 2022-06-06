@file:OptIn(ExperimentalMaterialApi::class)

package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components


import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.AnimatedImage
import com.example.cyclistance.feature_main_screen.data.repository.MappingRepositoryImpl

import com.example.cyclistance.theme.*


@Composable
private fun MappingBottomSheet(sheetContent: @Composable ColumnScope.() -> Unit) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
    )

    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = sheetContent,
        sheetPeekHeight = 0.dp) {
/*
        Button(onClick = {
            scope.launch {

                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }) {
            Text(text = "Expand/Collapse Bottom Sheet")
        }
    }

 */
    }
}

@Composable
fun BottomSheetSearchingAssistance(onClickButton: () -> Unit) {

    MappingBottomSheet {


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface)) {

                val (gripLine, searchAnimatedIcon, searchingText, cancelButton) = createRefs()

                Divider(modifier = Modifier.constrainAs(gripLine){
                    top.linkTo(parent.top, margin = 8.5.dp)
                    width = Dimension.percent(0.08f)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)

                },
                color = Black440,
                thickness = 1.5.dp)

                AnimatedImage(
                    imageId = R.drawable.ic_dark_magnifying_glass,
                    modifier = Modifier
                        .constrainAs(searchAnimatedIcon) {
                            top.linkTo(gripLine.top, margin = 12.dp)
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
                            top.linkTo(searchAnimatedIcon.bottom, margin = 10.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        })

                OutlinedButton(
                    onClick = onClickButton,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(width = 1.dp, color = Black300),
                    modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(cancelButton) {
                            top.linkTo(searchingText.bottom, margin = 4.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom, margin = 10.dp)
                        }) {
                    Text(
                        text = "CANCEL",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSurface,
                    )


                }
            }
        }
    }
}

@Composable
private fun BottomSheetRescue(displayedText: String) {
    MappingBottomSheet {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface)) {

                val (gripLine, animatedIcon, arrivedText) = createRefs()

                Divider(modifier = Modifier.constrainAs(gripLine){
                    top.linkTo(parent.top, margin = 8.5.dp)
                    width = Dimension.percent(0.08f)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)

                },
                    color = Black440,
                    thickness = 1.5.dp)


                Text(
                    text = displayedText,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .constrainAs(arrivedText) {
                            top.linkTo(gripLine.top, margin = 25.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        })


                AnimatedImage(
                    imageId = R.drawable.ic_dark_ellipsis,
                    modifier = Modifier
                        .constrainAs(animatedIcon) {
                            top.linkTo(arrivedText.bottom, margin = 10.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            width = Dimension.value(55.dp)
                            bottom.linkTo(parent.bottom, margin = 15.dp)

                        })

            }

        }

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
            modifier = Modifier.size(56.dp).shadow(elevation = 8.dp,shape = CircleShape, ),
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

@Composable
fun OnGoingRescueBottomSheet(estimatedTimeRemaining: String) {
    MappingBottomSheet() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface)) {

                val (gripLine, timeRemaining, roundedButtonSection) = createRefs()


                Divider(modifier = Modifier.constrainAs(gripLine){
                    top.linkTo(parent.top, margin = 8.5.dp)
                    width = Dimension.percent(0.08f)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)

                },
                    color = Black440,
                    thickness = 1.5.dp)


                Text(
                    text = "Estimated time of arrival: $estimatedTimeRemaining",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(timeRemaining) {
                        top.linkTo(gripLine.top, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })


                Row(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .constrainAs(roundedButtonSection) {
                            top.linkTo(timeRemaining.bottom, margin = 22.dp)
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
                        buttonSubtitle = "Cancel",onClick = {})
                }
            }
        }
    }
}



@Preview
@Composable
fun BottomSheetRescuerArrived() {
    BottomSheetRescue(displayedText = "Your rescuer has arrived.")
}

@Preview
@Composable
fun DestinationReachedBottomSheet() {
    BottomSheetRescue(displayedText = "You have reached your destination.")
}

@Preview
@Composable
fun SearchingAssistance() {
    CyclistanceTheme(true){
        BottomSheetSearchingAssistance(onClickButton = {})
    }
}



@Preview
@Composable
fun MappingBottomSheetPreview() {
    CyclistanceTheme(true){
        OnGoingRescueBottomSheet(estimatedTimeRemaining = "2mins")
    }
}


