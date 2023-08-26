package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
fun DeleteHazardousLaneMarkerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit) {

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
                text = "Are you sure you want to delete \nthis marker?",
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
private fun PreviewDeleteHazardousMarkerDialog() {
    CyclistanceTheme(darkTheme = true) {

        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            DeleteHazardousLaneMarkerDialog(
                onDismissRequest = { /*TODO*/ },
                onClickConfirmButton = { /*TODO*/ },
                modifier = Modifier)
        }
    }
}