package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun CancelSearchDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickOkay: () -> Unit
) {

    YesNoDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = "Are you sure you want to cancel \nthe search?",
        positiveButtonText = "Yes",
        negativeButtonText = "No",
        onClickNegativeButton = onDismissRequest,
        onClickPositiveButton = {
            onClickOkay()
            onDismissRequest()
        },
        )
}

@Preview
@Composable
fun PreviewCancelSearchDialog() {
    CyclistanceTheme{
        CancelSearchDialog(onDismissRequest = { /*TODO*/ }) {

        }
    }
}