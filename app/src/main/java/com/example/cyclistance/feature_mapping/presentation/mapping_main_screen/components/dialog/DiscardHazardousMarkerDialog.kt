package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DialogAnimatedIconCreator
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DiscardHazardousLaneMarkerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickDiscard: () -> Unit) {

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
                text = "Unsaved Changes",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(vertical = 4.dp)
                )

            Text(
                text = "Are you sure you want to discard \nthe changes you made?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
                color = Black500)


            Spacer(modifier = Modifier.height(16.dp))

            ButtonNavigation(
                modifier = Modifier.fillMaxWidth(),
                onClickNegativeButton = onDismissRequest,
                onClickPositiveButton = onClickDiscard, negativeButtonText = "Cancel", positiveButtonText = "Discard")

        }
    }
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