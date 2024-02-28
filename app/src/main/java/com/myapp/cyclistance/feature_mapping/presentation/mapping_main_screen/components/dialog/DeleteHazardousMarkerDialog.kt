package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.core.presentation.dialogs.yes_no_dialog.YesNoDialog
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun DeleteHazardousLaneMarkerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit) {


    YesNoDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        onClickNegativeButton = onDismissRequest,
        onClickPositiveButton = onClickConfirmButton,
        message = "Are you sure you want to delete \nthis marker?",
        )


}

@Preview
@Composable
private fun PreviewDeleteHazardousMarkerDialog() {
    CyclistanceTheme(darkTheme = true) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            DeleteHazardousLaneMarkerDialog(
                onDismissRequest = { /*TODO*/ },
                onClickConfirmButton = { /*TODO*/ },
                modifier = Modifier)
        }
    }
}