package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetReachedDestination(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit,
    onClickOkButton: () -> Unit) {

    BottomSheetRescue(
        modifier = modifier,
        isDarkTheme = isDarkTheme,
        displayedText = "You have reached your destination.",
        content = content,
        onClickOkButton = onClickOkButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState)
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DestinationReachedBottomSheetPreview() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Expanded))
    CyclistanceTheme(isDarkTheme) {
        BottomSheetReachedDestination(
            isDarkTheme = isDarkTheme,
            content = {},
            onClickOkButton = {},
            bottomSheetScaffoldState = bottomSheetScaffoldState)

    }

}


