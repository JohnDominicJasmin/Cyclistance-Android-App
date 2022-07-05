package com.example.cyclistance.feature_no_internet.presentation.components

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black300
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.core.utils.ConnectionStatus


@Composable
fun NoInternetScreen(
    isDarkTheme: Boolean = false,
    onBackPressed: () -> Unit,
    navigate: () -> Unit,
) {

    val context = LocalContext.current

    BackHandler(enabled = true, onBack = onBackPressed)


    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {


        ConstraintLayout(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 25.dp, end = 25.dp)
                .background(MaterialTheme.colors.background)) {

            val (image, title, description, retryButton, goToSettingsButton) = createRefs()


            Image(
                painter = painterResource(id = if(isDarkTheme) R.drawable.ic_dark_astronaut else R.drawable.ic_light_astronaut),
                contentDescription = "Astronaut",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.2f)
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    })

            Text(
                text = "No Internet Connection.",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onBackground,
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
                color = MaterialTheme.colors.onBackground,
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
                        navigate()
                    }
                },
                modifier = Modifier
                    .padding(4.dp)
                    .width(150.dp)
                    .constrainAs(retryButton) {
                        top.linkTo(description.bottom, margin = 40.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                border = BorderStroke(width = 2.dp, color = Black300),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground)) {

                Text(
                    text = "Try again",
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    style = MaterialTheme.typography.button,
                )

            }


            TextButton(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary),
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
                    color = MaterialTheme.colors.primary)


            }

        }

    }

}

@Preview(device = Devices.NEXUS_5)
@Composable
fun NoInternetScreenPreview() {
    val isDarkTheme = true
    CyclistanceTheme(isDarkTheme) {
        NoInternetScreen(onBackPressed = {  }, isDarkTheme = isDarkTheme) {

        }
    }
}