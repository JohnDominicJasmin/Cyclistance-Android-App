package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@Composable
fun SignInButton() {

    Box(
        modifier = Modifier
            .layoutId(layoutId = ConstraintsItem.SignInButton.layoutId)
            .wrapContentSize().shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
        ,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .wrapContentHeight()
                .width(200.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = ThemeColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Sign In", color = Color.Black)
        }
    }

}