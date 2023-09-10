package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun CancelOnGoingRescueDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickOkay: () -> Unit
) {

    YesNoDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = "Are you sure you want to cancel \nthe rescue?",
        message = "You are about to cancel the rescue. This will end the ongoing rescue and you will be redirected to the.",
        positiveButtonText = "Yes",
        negativeButtonText = "No",
        onClickNegativeButton = onDismissRequest,
        onClickPositiveButton = {
            onClickOkay()
            onDismissRequest()
        })

}


@Preview
@Composable
fun PreviewCancelOnGoingRescueDialog() {
    CyclistanceTheme(darkTheme = true) {
        CancelOnGoingRescueDialog(onDismissRequest = { /*TODO*/ }, onClickOkay = {})
    }
}