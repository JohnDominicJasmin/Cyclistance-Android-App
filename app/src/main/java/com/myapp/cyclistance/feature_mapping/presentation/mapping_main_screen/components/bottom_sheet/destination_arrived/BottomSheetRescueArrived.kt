package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.destination_arrived

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.BottomSheetRescue
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetRescueArrived(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickOkButton: () -> Unit) {

    BottomSheetRescue(
        modifier = modifier,
        displayedText = "Your rescuer has arrived.",
        onClickOkButton = onClickOkButton,
        bottomSheetScaffoldState = bottomSheetScaffoldState)
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomSheetRescuerArrivedPreview() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(isDarkTheme) {
            BottomSheetRescueArrived(
                onClickOkButton = {},
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }
    }
}