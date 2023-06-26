package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetReachedDestination(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickOkButton: () -> Unit) {

    BottomSheetRescue(
        modifier = modifier,
        displayedText = "You have reached your destination.",
        onClickOkButton = onClickOkButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState)
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DestinationReachedBottomSheetPreview() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))

    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(isDarkTheme) {
            BottomSheetReachedDestination(
                onClickOkButton = {},
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }
    }

}


