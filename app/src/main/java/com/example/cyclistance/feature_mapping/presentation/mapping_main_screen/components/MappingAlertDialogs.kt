package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components


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
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingDialogState


@Preview
@Composable
fun Preview() {

    val dialogState = remember{ mutableStateOf(true) }

    AlertDialogCreator(dialogState,
        onClickDismissButton = {
         dialogState.value = false
    }, onClickConfirmButton = {

    }) { dismiss, confirm ->


        AlertDialogContent(
            mappingDialogData = MappingDialogState(
                icon = R.drawable.ic_question,
                title = "Are you sure?",
                description = "You want to cancel rescue request?"),
            onDismiss = dismiss,
            onConfirm = confirm)

    }
}


@Composable
fun AlertDialogCreator(
    show: MutableState<Boolean>,
    onClickDismissButton: () -> Unit,
    onClickConfirmButton: () -> Unit,
    content: @Composable (
        onDismiss: () -> Unit,
        onConfirm: () -> Unit, ) -> Unit
) {
    if (show.value) {
    Dialog(onDismissRequest = { show.value = false }, properties = DialogProperties()) {

            content(onClickDismissButton, onClickConfirmButton)
        }
    }
}



@Composable
fun AlertDialogContent(
    mappingDialogData: MappingDialogState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = BackgroundColor ) {

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

            MappingButtonNavigation(
                modifier = Modifier.wrapContentHeight().fillMaxWidth(0.8f),
                onClickCancelButton = onDismiss,
                onClickConfirmButton = onConfirm
            )






        }
    }
}

