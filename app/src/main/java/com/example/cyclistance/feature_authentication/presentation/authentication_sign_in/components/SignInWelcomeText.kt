package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
@Preview
@Composable
fun SignUpTextArea() {
    Column(modifier = Modifier.layoutId(AuthenticationConstraintsItem.WelcomeTextArea.layoutId)) {


        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle()){
                    append("Welcome to Cyclistance\n Login to your Account")
                }
            },
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Bold
        )
    }
}