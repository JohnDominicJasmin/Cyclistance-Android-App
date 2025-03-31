package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun DiscardHazardousLaneMarkerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickDiscard: () -> Unit) {

    YesNoDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Unsaved Changes",
        message = "Are you sure you want to discard \nthe changes you made?",
        onClickNegativeButton = onDismissRequest,
        onClickPositiveButton = onClickDiscard,
        negativeButtonText = "Cancel",
        positiveButtonText = "Discard"
    )
}


@Preview
@Composable
fun PreviewDiscardHazardousLaneMarkerDialog() {
    CyclistanceTheme(darkTheme = true) {
        DiscardHazardousLaneMarkerDialog(
            onDismissRequest = { /*TODO*/ },
            onClickDiscard = { /*TODO*/ })
    }
}