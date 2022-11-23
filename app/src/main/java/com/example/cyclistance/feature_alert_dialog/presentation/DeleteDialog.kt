package com.example.cyclistance.feature_alert_dialog.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.presentation.utils.DialogCreator
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red700


@Composable
fun DeleteAccountDialog(
    icon: Int,
    onDismissRequest: () -> Unit = {}) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }
    val (text, onValueChangeText) = rememberSaveable { mutableStateOf("") }
    val isDeletingAccountConfirm = remember(text) { text == "delete account" }

    DialogCreator(
        icon = icon,
        isDialogOpen = isDialogOpen,
        onDialogVisibilityToggle = {
            onDialogVisibilityToggle(!isDialogOpen)
        },
        onDismissRequest = onDismissRequest) {


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            Text(
                "Delete Account?",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 9.dp))



            Text(
                text = "You'll permanently lose your:",
                color = Black500,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 6.5.dp))


            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = ParagraphStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 18.sp)) {
                        append("- profile\n")
                        append("- messages\n")
                        append("- photos")
                    }
                },
                color = Black500,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 8.dp))


            Text(
                text = "Type \"delete account\" to confirm",
                color = Black500,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center),
            )
            BasicTextField(
                value = text,
                onValueChange = onValueChangeText,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .border(width = 1.dp, color = Black500)
                    .widthIn(max = 400.dp)
                    .height(30.dp)
                    .fillMaxWidth(0.85f)
                    .padding(start = 5.dp, top = 5.dp, end = 5.dp),
                singleLine = true,
                maxLines = 1,
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.onBackground,
                    1.00f to MaterialTheme.colors.onBackground),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface
                )
            )


            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = { },
                    contentPadding = PaddingValues(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(1.dp, Black500),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Cancel",
                        color = Black500,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium))
                }


                OutlinedButton(
                    enabled = isDeletingAccountConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { },
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Red700),

                    ) {
                    Text(
                        text = "Confirm",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium))
                }
            }
        }
    }
}



@Preview(name = "DeleteDialog")
@Composable
private fun PreviewDeleteDialog() {

    CyclistanceTheme(true) {
        DeleteAccountDialog(
            icon = R.drawable.ic_trash_can
        )
    }
}