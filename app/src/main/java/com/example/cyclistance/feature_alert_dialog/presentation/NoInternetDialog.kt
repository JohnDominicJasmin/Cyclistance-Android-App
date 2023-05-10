package com.example.cyclistance.feature_alert_dialog.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun NoInternetDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current
    val dialogOpen = rememberSaveable { mutableStateOf(true) }


    if (dialogOpen.value) {

        Dialog(
            onDismissRequest = {
                onDismiss()
                dialogOpen.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true),
        ) {

            Surface(
                modifier = modifier
                    .fillMaxWidth(0.90f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colors.surface) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_dark_astronaut),
                        contentDescription = "No Internet Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(115.dp)
                            .padding(top = 20.dp)
                    )

                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Spacer(modifier = Modifier.height(25.dp))

                        Text(
                            text = "No Internet Connection",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                            modifier = Modifier,
                            color = MaterialTheme.colors.onSurface,
                        )

                        Text(
                            text = "No Internet connection. Make sure Wi-Fi \n" +
                                   "or mobile data is turned on, then try again.",
                            style = MaterialTheme.typography.subtitle1,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 6.dp, start = 25.dp, end = 25.dp),
                            color = Black500,
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                dialogOpen.value = false
                                onDismiss()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary)) {
                            Text(text = "Ok", style = MaterialTheme.typography.button)
                        }


                        TextButton(
                            onClick = {
                                dialogOpen.value = false
                                context.startActivity(Intent(Settings.ACTION_SETTINGS))
                            }, colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = MaterialTheme.colors.primary),
                            modifier = Modifier.wrapContentWidth()) {
                            Text(
                                text = "Go to Settings",
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.primary)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewInfoDialog() {
    CyclistanceTheme(true) {
        Box(modifier = Modifier.fillMaxWidth()) {
            NoInternetDialog {}
        }
    }
}

