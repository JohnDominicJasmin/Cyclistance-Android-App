@file:OptIn(ExperimentalMaterialApi::class)

package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue.BottomSheetOnGoingRescue
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
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
        modifier = modifier.fillMaxWidth(),
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
    )


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








@Composable
fun OutlinedActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Black500),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )


    }
}
