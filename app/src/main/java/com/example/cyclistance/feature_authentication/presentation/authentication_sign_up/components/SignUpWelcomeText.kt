package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

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
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Preview
@Composable
fun SignUpTextArea() {
    Column(modifier = Modifier.layoutId(AuthenticationConstraintsItem.WelcomeTextArea.layoutId)) {

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle()){
                    append("Welcome to Cyclistance\n Sign Up your Account")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
            ),

        )

    }
}