package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.*


private val constrains = ConstraintSet() {
    val appIcon = createRefFor(id = "app_icon")
    val topWave = createRefFor(id = "top_wave")
    val bottomWave = createRefFor(id = "bottom_wave")
    val welcomeTextArea = createRefFor(id = "text_area")
    val textFieldArea = createRefFor(id = "text_field_area")
    val facebookAndGoogle = createRefFor(id = "fb_and_google")

    constrain(appIcon) {
        top.linkTo(parent.top, margin = 15.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(welcomeTextArea) {
        top.linkTo(appIcon.bottom, margin = 10.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(topWave) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(bottomWave) {
        bottom.linkTo(parent.bottom)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(textFieldArea) {
        top.linkTo(welcomeTextArea.bottom, margin = 27.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(facebookAndGoogle){
        top.linkTo(textFieldArea.bottom, margin = 27.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = Color.White,
    backgroundColor = TextFieldBackgroundColor,
    focusedIndicatorColor = ThemeColor,
    unfocusedIndicatorColor = ThemeColor,
    cursorColor = Color.White

)

@Composable
fun SignInScreen() {


    ConstraintLayout(
        constraintSet = constrains, modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {



        AppImageIcon()
        TextArea()
        Waves()
        TextFieldsArea()

        Row(
            modifier = Modifier
                .layoutId("fb_and_google")
                .fillMaxWidth(fraction = 0.7f)){

            Button(onClick = {  }) {
                Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google Sign In", tint = Color.Unspecified)
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Google", color = GoogleButtonTextColor)
            }


        }














    }


}

@Composable
private fun AppImageIcon() {
    CustomImage(
        layoutId = "app_icon",
        contentDescription = "App Icon",
        imageId = R.drawable.ic_cyclistance_app_icon,
        modifier = Modifier
            .height(100.dp)
            .width(90.dp)
    )
}
@Composable
fun TextArea() {
    Column(modifier = Modifier.layoutId("text_area")) {

        Text(
            text = "Welcome  to  Cyclistance",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "   Login to your account",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun Waves() {
    CustomImage(
        layoutId = "top_wave",
        contentDescription = "Top wave",
        imageId = R.drawable.ic_top_wave
    )

    CustomImage(
        layoutId = "bottom_wave",
        contentDescription = "Bottom wave",
        imageId = R.drawable.ic_bottom_wave
    )
}

@Composable
private fun TextFieldsArea(){
    Column(
        modifier = Modifier
            .layoutId("text_field_area")
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = email,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    color = TextFieldColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = TextFieldColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = textFieldColors(),
        )

        /*
        val textColor = if (true) {
            MaterialTheme.colors.error
        } else {
            MaterialTheme.colors.onSurface
        }
        Text(
            textAlign = TextAlign.Center,
            text = if (true) "Requires '@' and at least 5 symbols" else "Helper message",
            style = MaterialTheme.typography.caption.copy(color = textColor),
        )
         */



        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            label = {
                Text(
                    text = "Password",
                    color = TextFieldColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = TextFieldColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = textFieldColors()
        )

    }

}

@Composable
private fun CustomImage(
    layoutId: String,
    contentDescription: String? = null,
    imageId: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.layoutId(layoutId)) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            modifier = modifier, contentScale = ContentScale.Fit
        )
    }
}


@Preview
@Composable
fun PreviewSignInScreen() {
    SignInScreen()
}