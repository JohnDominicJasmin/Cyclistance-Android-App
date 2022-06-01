package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun SignInButton(onClickButton: ()-> Unit) {

    Box(
        modifier = Modifier
            .layoutId(layoutId = AuthenticationConstraintsItem.SignInButton.layoutId)
            .wrapContentSize()
        ,
        contentAlignment = Alignment.Center) {
        Button(

            onClick = onClickButton,
            modifier = Modifier
                .height(50.dp)
                .width(220.dp)
                .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Sign In", color = Color.Black, fontSize = 16.sp)
        }
    }

}