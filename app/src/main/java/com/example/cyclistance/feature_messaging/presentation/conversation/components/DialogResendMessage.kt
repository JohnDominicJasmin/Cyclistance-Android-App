package com.example.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DialogResendMessage(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onClickResend: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = true
        )) {

        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Resend message?",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(top = 12.dp)
                )

                ButtonNavigation(
                    modifier = Modifier.fillMaxWidth(),
                    negativeButtonEnabled = true,
                    positiveButtonEnabled = true,
                    negativeButtonText = "Cancel",
                    positiveButtonText = "Resend",
                    onClickNegativeButton = onDismiss,
                    onClickPositiveButton = { onDismiss(); onClickResend() }
                )

            }

        }

    }
}

@Preview(name = "Dark theme")
@Composable
fun PreviewDialogResendMessage1() {
    CyclistanceTheme(darkTheme = true) {
        DialogResendMessage(onClickResend = {}, onDismiss = {})
    }
}

@Preview(name = "Light theme")
@Composable
fun PreviewDialogResendMessage2() {
    CyclistanceTheme(darkTheme = false) {
        DialogResendMessage(onClickResend = {}, onDismiss = {})
    }
}