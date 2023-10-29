package com.example.cyclistance.core.presentation.dialogs.yes_no_dialog

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
fun YesNoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    title: String? = null,
    message: String? = null,
    negativeButtonEnabled: Boolean = true,
    positiveButtonEnabled: Boolean = true,
    negativeButtonText: String = "Cancel",
    positiveButtonText: String = "Confirm",
    onClickNegativeButton: () -> Unit = {},
    onClickPositiveButton: () -> Unit = {}) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    DialogAnimatedIconCreator(
        animatedResIcon = R.raw.question,
        isDialogOpen = isDialogOpen,
        onDialogVisibilityToggle = { onDialogVisibilityToggle(!isDialogOpen) },
        onDismissRequest = onDismissRequest) {


        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            title?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            message?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
                    color = Black500)
            }



            Spacer(modifier = Modifier.height(16.dp))

            ButtonNavigation(
                modifier = Modifier.fillMaxWidth(),
                negativeButtonEnabled = negativeButtonEnabled,
                positiveButtonEnabled = positiveButtonEnabled,
                negativeButtonText = negativeButtonText,
                positiveButtonText = positiveButtonText,
                onClickNegativeButton = onClickNegativeButton,
                onClickPositiveButton = onClickPositiveButton
            )


        }

    }

}


@Preview
@Composable
fun PreviewYesNoDialog() {
    CyclistanceTheme(darkTheme = true){
        YesNoDialog(onDismissRequest = { /*TODO*/ }, title = "Title Sample", message = "oansdoiasd")

    }
}