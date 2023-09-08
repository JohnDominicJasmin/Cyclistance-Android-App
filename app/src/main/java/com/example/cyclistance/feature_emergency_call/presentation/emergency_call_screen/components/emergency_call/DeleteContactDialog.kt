package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DeleteContactDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit,
    nameToDelete: String) {


    YesNoDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        message = "Are you sure you want to delete\n" +
                  "“$nameToDelete”?",
        onClickNegativeButton = onDismissRequest,
        onClickPositiveButton = onClickConfirmButton,
        )

}

@Preview
@Composable
fun PreviewEmergencyCallDeleteDialog() {
    CyclistanceTheme(darkTheme = true) {
        DeleteContactDialog(
            onDismissRequest = {},
            onClickConfirmButton = {},
            nameToDelete = "Philippine Red Cross")
    }
}