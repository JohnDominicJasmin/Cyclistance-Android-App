@file:OptIn(ExperimentalMaterialApi::class)

package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black300
import com.example.cyclistance.theme.Black440

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
        modifier = modifier,
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
fun RoundedButtonItem(
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




