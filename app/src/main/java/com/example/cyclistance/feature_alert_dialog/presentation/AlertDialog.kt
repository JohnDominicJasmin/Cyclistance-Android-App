package com.example.cyclistance.feature_alert_dialog.presentation

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.utils.DialogCreator
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun AlertDialog(
    alertDialog: AlertDialogModel,
    onDismissRequest: () -> Unit = {}) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    DialogCreator(
        icon = alertDialog.icon,
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
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                alertDialog.description,
                style = TextStyle(fontSize = 14.sp),
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
                    style = MaterialTheme.typography.button)

            }
        }


    }

}








@Preview
@Composable
fun AlertDialogPreview() {

    CyclistanceTheme(true) {
        AlertDialog(
            alertDialog = AlertDialogModel(
                title = "Success!",
                description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam, quod.",
                icon = io.github.farhanroy.composeawesomedialog.R.raw.success),
            onDismissRequest = {})

    }

}

