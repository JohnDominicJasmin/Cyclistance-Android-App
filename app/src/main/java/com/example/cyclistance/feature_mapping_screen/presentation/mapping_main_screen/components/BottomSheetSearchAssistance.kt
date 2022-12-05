package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.AnimatedImage
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSearchingAssistance(
    isDarkTheme: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit,
    onClickCancelSearchButton: () -> Unit) {

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
                        text = "Requesting help to nearby Cyclists…",
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
                            onClickCancelSearchButton()
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchingAssistancePreview() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(
        initialValue =BottomSheetValue.Expanded))

    CyclistanceTheme(isDarkTheme) {
        BottomSheetSearchingAssistance(
            isDarkTheme = isDarkTheme,
            onClickCancelSearchButton = {},
            content = {},
            bottomSheetScaffoldState = bottomSheetScaffoldState)
    }
}