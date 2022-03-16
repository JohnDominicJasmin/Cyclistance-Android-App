package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState

@Composable
fun ErrorText(text:String) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        style = MaterialTheme.typography.caption.copy(color =  MaterialTheme.colors.error)

        )

}