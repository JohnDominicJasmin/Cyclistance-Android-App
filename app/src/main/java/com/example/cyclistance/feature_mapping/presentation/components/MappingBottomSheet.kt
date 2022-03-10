package com.example.cyclistance.feature_mapping.presentation.components


import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.AnimatedImage

import com.example.cyclistance.feature_authentication.presentation.theme.ShowableDisplaysBackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ShowableDisplaysBorderColor


@OptIn(ExperimentalMaterialApi::class)
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
fun BottomSheetSearchingAssistance(onClickButton:()->Unit) {

    MappingBottomSheet() {


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = ShowableDisplaysBackgroundColor)) {

                    val (searchAnimatedIcon, searchingText, cancelButton) = createRefs()



                    AnimatedImage(
                        imageId = R.drawable.ic_magnifying_search,
                        modifier = Modifier
                            .constrainAs(searchAnimatedIcon){
                                top.linkTo(parent.top, margin = 12.dp)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                height = Dimension.value(50.dp)
                                width = Dimension.value(50.dp)
                            })




                    Text(
                        text = "Searching for nearby assistance...",
                        color = Color.White,
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
                        border = BorderStroke(width = 1.dp, color = ShowableDisplaysBorderColor),
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
                            color = Color.White
                        )


                    }
                }
            }
        }
    }


@Preview
@Composable
fun Preview1() {
    BottomSheetSearchingAssistance(){

    }
}
@Composable
fun BottomSheetRescuerArrived() {
    MappingBottomSheet(){

    }
}

