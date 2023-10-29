package com.example.cyclistance.core.presentation.dialogs.alert_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.common.DialogAnimatedIconCreator
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    alertDialog: AlertDialogState,
    onDismissRequest: () -> Unit = {}) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    DialogAnimatedIconCreator(
        modifier = modifier,
        animatedResIcon = alertDialog.icon,
        isDialogOpen = isDialogOpen,
        onDialogVisibilityToggle = { onDialogVisibilityToggle(!isDialogOpen) },
        onDismissRequest = onDismissRequest
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                alertDialog.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colors.onSurface)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                alertDialog.description,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = Black500)

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    onDialogVisibilityToggle(false)
                    onDismissRequest()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary)) {

                Text(
                    text = "Ok",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button,
                )

            }
        }


    }

}








@Preview
@Composable
fun PreviewAlertDialogDark() {

    CyclistanceTheme(true) {
        AlertDialog(
            alertDialog = AlertDialogState(
                title = "Successfully Updated the Password!",
                description = "You can now login using your updated password",
                icon = R.raw.success),
            onDismissRequest = {})

    }
}

@Preview
@Composable
fun PreviewAlertDialogLight() {
    CyclistanceTheme(false) {
        AlertDialog(
            alertDialog = AlertDialogState(
                title = "Success!",
                description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam, quod.",
                icon = R.raw.question),
            onDismissRequest = {})
    }
}

