@file:OptIn(ExperimentalMaterialApi::class)

package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
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
        modifier = Modifier.fillMaxWidth(),
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
    )


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
