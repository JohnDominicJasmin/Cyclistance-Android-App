package com.example.cyclistance.feature_dialogs.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.feature_dialogs.domain.model.DialogModel
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DialogCreator(
    modifier: Modifier = Modifier,
    dialogModel: DialogModel,
    onDismiss: () -> Unit,
    onClickGoToSettings: () -> Unit,
) {
    var dialogOpen by rememberSaveable { mutableStateOf(true) }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = {
                onDismiss()
                dialogOpen = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true),
        ) {

            Surface(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colors.surface) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        painter = painterResource(id = dialogModel.icon),
                        contentDescription = dialogModel.iconContentDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .weight(0.06f, fill = false)
                            .padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))


                    Text(
                        text = dialogModel.title,
                        style = MaterialTheme.typography.subtitle1.copy(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface),
                        modifier = Modifier
                            .weight(0.1f, fill = false)
                            .padding(horizontal = 12.dp),
                    )



                    Text(
                        text = dialogModel.description,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .padding(top = 6.dp, start = 25.dp, end = 25.dp)
                            .weight(0.15f, fill = false),
                        color = Black500,
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = {
                            dialogOpen = false
                            onClickGoToSettings()
                        }, colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary),
                        shape = RoundedCornerShape(12.dp)) {
                        Text(
                            text = dialogModel.firstButtonText,
                            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Medium),
                        )
                    }


                    TextButton(
                        onClick = {
                            dialogOpen = false
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.28f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Black440)) {
                        Text(
                            text = dialogModel.secondButtonText,
                            style = MaterialTheme.typography.button)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "LightTheme")
@Composable
fun PreviewDialogCreatorLight() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
            DialogCreator(
                dialogModel = DialogModel(
                    icon = R.drawable.ic_no_internet_connection,
                    iconContentDescription = "sample",
                    title = "Title Sample",
                    description = "No Internet Connection. Make sure Wi-Fi \n" +
                                  "or mobile data is turned on, then try again.",
                    firstButtonText = "Go to Settings",
                    secondButtonText = "Not now",
                ),
                onDismiss = {},
                onClickGoToSettings = {}
            )
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "DarkTheme")
@Composable
fun PreviewDialogCreatorDark() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogCreator(
                dialogModel = DialogModel(
                    icon = R.drawable.ic_notification_permission,
                    iconContentDescription = "sample",
                    title = "Title Sample Title Sample Title Sample Title Sample",
                    description = "No Internet Connection. Make sure Wi-Fi \n" +
                                  "or mobile data is turned on, then try again.",
                    firstButtonText = "Go to Settings",
                    secondButtonText = "Not now",
                ),
                onDismiss = {},
                onClickGoToSettings = {}
            )
        }
    }
}