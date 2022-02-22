package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun SignUpTextArea() {
    Column(modifier = Modifier.layoutId(AuthenticationConstraintsItem.WelcomeTextArea.layoutId)) {

        Text(
            text = "Sign up your account",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )


    }
}