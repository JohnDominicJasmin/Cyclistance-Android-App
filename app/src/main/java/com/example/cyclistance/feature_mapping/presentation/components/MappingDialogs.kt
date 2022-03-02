package com.example.cyclistance.feature_mapping.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@Composable
fun Preview() {
    val dialogState = remember{ mutableStateOf(true) }
    RescueDialogCreator(dialogState,
        onClickDismissButton = {
         dialogState.value = false
    }, onClickConfirmButton = {

    }) { dismiss, confirm ->

        /*
        RescueRequestDialogContent(
            mappingDialogData = MappingDialogData(
                icon = R.drawable.ic_rescue_request,
                title = "Rescue request!",
                description = "John Doe is requesting a rescue."),
            onDismiss = dismiss,
            onConfirm = confirm)

         */

        DialogContent(
            mappingDialogData = MappingDialogData(
                icon = R.drawable.ic_question,
                title = "Are you sure?",
                description = "You want to cancel rescue request?"),
            onDismiss = dismiss,
            onConfirm = confirm)

    }
}


@Composable
fun RescueDialogCreator(
    show: MutableState<Boolean>,
    onClickDismissButton: () -> Unit,
    onClickConfirmButton: () -> Unit,
    content: @Composable (
        onDismiss: () -> Unit,
        onConfirm: () -> Unit, ) -> Unit
) {
    Dialog(onDismissRequest = { show.value = false }, properties = DialogProperties()) {

        if (show.value) {
            content(onClickDismissButton, onClickConfirmButton)
        }
    }
}


@Composable
fun DialogContent(
    mappingDialogData: MappingDialogData,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = BackgroundColor) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 17.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {


            Icon(
                painter = painterResource(id = mappingDialogData.icon),
                contentDescription = null,
                tint = Color.Unspecified)

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = mappingDialogData.title,
                style = MaterialTheme.typography.h6,
                color = Color.White,
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = mappingDialogData.description,
                style = MaterialTheme.typography.subtitle1,
                color = Color(0xFF979797),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal)


            Spacer(modifier = Modifier.height(18.dp))


            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)) {

                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(all = 3.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8C8C8C)),
                    shape = RoundedCornerShape(6.dp),
                    onClick = onDismiss) {
                    Text(
                        text = "Dismiss",
                        style = MaterialTheme.typography.button,
                        color = Color.Black,
                    )
                }


                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(all = 3.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ThemeColor),
                    shape = RoundedCornerShape(6.dp),
                    onClick = onConfirm) {


                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.button,
                        color = Color.Black,
                    )
                }


            }


        }
    }
}


