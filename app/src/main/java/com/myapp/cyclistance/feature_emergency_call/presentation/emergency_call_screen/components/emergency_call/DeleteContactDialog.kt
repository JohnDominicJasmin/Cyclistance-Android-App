package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun DeleteContactDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onClickConfirmButton: () -> Unit,
    nameToDelete: String) {


    YesNoDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        message = "Are you sure you want to delete\n" +
                  "“$nameToDelete”?",
        onClickNegativeButton = onDismiss,
        onClickPositiveButton = onClickConfirmButton,
        )

}

@Preview
@Composable
fun PreviewEmergencyCallDeleteDialog() {
    CyclistanceTheme(darkTheme = true) {
        DeleteContactDialog(
            onDismiss = {},
            onClickConfirmButton = {},
            nameToDelete = "Philippine Red Cross")
    }
}