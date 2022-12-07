package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red900

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetOnGoingRescue(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    estimatedTimeRemaining: String) {


    MappingBottomSheet(
        modifier = modifier,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .shadow(elevation = 12.dp, shape =  RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.surface)) {

                    val (timeRemaining, roundedButtonSection) = createRefs()

                    val estimatedTimeArrival = remember(estimatedTimeRemaining){
                        derivedStateOf {
                            if (estimatedTimeRemaining.isNotEmpty()) {
                                "Estimated time of arrival: $estimatedTimeRemaining"
                            } else {
                                "Calculating estimated time of arrival..."
                            }
                        }
                    }

                    Text(
                        text = estimatedTimeArrival.value,
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
                            buttonSubtitle = "Call", onClick = onClickCallButton)


                        RoundedButtonItem(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                            imageId = R.drawable.ic_chat,
                            buttonSubtitle = "Chat", onClick = onClickChatButton)


                        RoundedButtonItem(
                            backgroundColor = Red900,
                            contentColor = Color.White,
                            imageId = R.drawable.ic_cancel_1,
                            buttonSubtitle = "Cancel", onClick = onClickCancelButton)
                    }
                }
            }
        },
        content = content)

}


@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "BottomSheetOnGoingRescue")
@Composable
private fun PreviewBottomSheetOnGoingRescue() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(
        initialValue =BottomSheetValue.Expanded))
    CyclistanceTheme(true) {
        BottomSheetOnGoingRescue(
            estimatedTimeRemaining = "2mins",
            content = {},
            onClickCancelButton = {},
            onClickCallButton = {},
            onClickChatButton = {},
            bottomSheetScaffoldState = bottomSheetScaffoldState)
    }
}