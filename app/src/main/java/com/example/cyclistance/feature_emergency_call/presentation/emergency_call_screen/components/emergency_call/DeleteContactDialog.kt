package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DialogAnimatedIconCreator
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DeleteContactDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit,
    nameToDelete: String) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    DialogAnimatedIconCreator(
        icon = R.raw.question,
        isDialogOpen = isDialogOpen,
        onDialogVisibilityToggle = { onDialogVisibilityToggle(!isDialogOpen) },
        onDismissRequest = onDismissRequest) {


        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Are you sure you want to delete\n" +
                       "“$nameToDelete”?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface)


            Spacer(modifier = Modifier.height(16.dp))

            ButtonNavigation(
                modifier = Modifier.fillMaxWidth(0.8f),
                onClickNegativeButton = onDismissRequest,
                onClickPositiveButton = onClickConfirmButton)

        }
    }
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