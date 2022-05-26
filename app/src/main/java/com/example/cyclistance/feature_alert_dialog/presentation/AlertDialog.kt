package com.example.cyclistance.feature_alert_dialog.presentation

import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.theme.ThemeColor
import io.github.farhanroy.composeawesomedialog.themes.Shapes


data class AlertDialogData(
    var title: String = "",
    var description: String = "",
    @RawRes var resId: Int = 0
)

@Composable
fun SetupAlertDialog(
    alertDialog: AlertDialogData,
    onDismissRequest: () -> Unit) {

    val openDialog = remember { mutableStateOf(true) }


    if (openDialog.value) {
        Dialog(onDismissRequest = {
            onDismissRequest()
            openDialog.value = false


        }, properties = DialogProperties()) {
            Box(
                Modifier
                    .width(300.dp)
                    .wrapContentHeight()) {
                Column(
                    Modifier
                        .width(300.dp)
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.height(36.dp))
                    Box(
                        Modifier
                            .width(300.dp)
                            .wrapContentHeight()
                            .background(
                                color = BackgroundColor,
                                shape = Shapes.large)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(
                                alertDialog.title,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                alertDialog.description,
                                style = TextStyle(fontSize = 14.sp),
                                color = Color.White)
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    modifier = Modifier.width(100.dp),
                                    onClick = {
                                        openDialog.value = false
                                              onDismissRequest()
                                              },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = ThemeColor,
                                        contentColor = Color.White)) {

                                    Text(
                                        text = "Ok",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.button)

                                }
                            }
                        }
                    }
                }

                DialogHeader(
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.TopCenter)
                        .border(
                            border = BorderStroke(width = 5.dp, color = Color.White),
                            shape = CircleShape
                        ), alertDialog.resId)
            }
        }
    }
}

@Composable
private fun DialogHeader(modifier: Modifier, @RawRes resId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
    )
}