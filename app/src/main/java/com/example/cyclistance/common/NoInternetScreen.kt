package com.example.cyclistance.common

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@Composable
fun NoInternetScreen(navController: NavHostController, onBackPressed: () -> Unit) {

    val context = LocalContext.current

    BackHandler(enabled = true, onBack = onBackPressed)


    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {


        ConstraintLayout(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 25.dp, end = 25.dp)
                .background(BackgroundColor)) {

            val (image, title, description, retryButton, goToSettingsButton) = createRefs()


            Image(
                painter = painterResource(id = R.drawable.ic_astronaut),
                contentDescription = "Astronaut",
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    })

            Text(
                text = "No Internet Connection.",
                style = MaterialTheme.typography.h6,
                color = Color.White,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(image.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
            )


            Text(
                text = "No Internet connection. Make sure Wi-Fi \n" +
                       "or mobile data is turned on, then try again.",
                style = MaterialTheme.typography.subtitle1,
                color = Color.White,
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(title.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
            )

            OutlinedButton(
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    if (ConnectionStatus.hasInternetConnection(context)) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .padding(4.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp), clip = true)
                    .width(150.dp)
                    .constrainAs(retryButton) {
                        top.linkTo(description.bottom, margin = 40.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                border = BorderStroke(width = 2.dp, color = Color(0xFFDDDDDD)),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = BackgroundColor,
                    contentColor = Color.White)) {

                Text(
                    text = "Try again",
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )

            }


            TextButton(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = ThemeColor),
                modifier = Modifier
                    .padding(1.dp)
                    .wrapContentWidth()
                    .constrainAs(goToSettingsButton) {
                        top.linkTo(retryButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
            ) {
                Text(
                    text = "Go to Settings",
                    style = MaterialTheme.typography.button,
                    color = ThemeColor)


            }

        }

    }

}